package com.deliveranything.domain.product.product.service;

import com.deliveranything.domain.product.product.entity.Product;
import com.deliveranything.domain.product.product.event.ProductKeywordsChangedEvent;
import com.deliveranything.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordGenerationService {

  private final ProductRepository productRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final WebClient.Builder webClientBuilder;

  @Value("${gemini.api.key}")
  private String geminiApiKey;

  @Value("${gemini.api.url}")
  private String geminiApiUrl;

  @Async
  @Transactional
  public void generateAndSaveKeywords(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

    String prompt = createPrompt(product.getName(), product.getDescription());

    GeminiRequest.Content promptContent = new GeminiRequest.Content(List.of(new GeminiRequest.Part(prompt)));
    GeminiRequest request = new GeminiRequest(List.of(promptContent));

    try {
      GeminiResponse response = webClientBuilder.build()
          .post()
          .uri(geminiApiUrl)
          .header("x-goog-api-key", geminiApiKey)
          .body(Mono.just(request), GeminiRequest.class)
          .retrieve()
          .bodyToMono(GeminiResponse.class)
          .block();

      if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
        String keywords = response.candidates().getFirst().content().parts().getFirst().text().trim();
        product.setKeywords(keywords);
        log.info("Generated keywords for product {}: {}", productId, keywords);

        eventPublisher.publishEvent(new ProductKeywordsChangedEvent(
            product.getStore().getId(), product.getId()
        ));
      }

    } catch (Exception e) {
      log.error("Failed to generate keywords for product {}: {}", productId, e.getMessage(), e);
    }
  }

  private String createPrompt(String productName, String productDescription) {
    return String.format("""
                다음 상품 정보에 대한 검색 키워드를 5~10개 사이로, 쉼표(,)로 구분하여 생성해줘.
                - 키워드는 명사 형태여야 함
                - 상품의 특징, 재료, 용도, 감성 등을 잘 나타내야 함
                - 다른 설명 없이 키워드만 응답해야 함

                [상품 정보]
                - 상품명: %s
                - 상품설명: %s

                [생성 예시]
                달콤한,디저트,선물용,수제,딸기케이크

                [키워드]
                """, productName, productDescription);
  }

  // --- DTO for Gemini API ---
  private record GeminiRequest(List<Content> contents) {
    private record Content(List<Part> parts) {}
    private record Part(String text) {}
  }

  private record GeminiResponse(List<Candidate> candidates) {
    private record Candidate(Content content) {}
    private record Content(List<Part> parts) {}
    private record Part(String text) {}
  }
}