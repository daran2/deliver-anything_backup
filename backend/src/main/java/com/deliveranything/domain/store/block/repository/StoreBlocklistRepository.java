package com.deliveranything.domain.store.block.repository;

import com.deliveranything.domain.store.block.entity.StoreBlocklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBlocklistRepository extends JpaRepository<StoreBlocklist, Long> {

}
