package com.yff.base.service;

import com.yff.base.domain.Bid;

import java.util.List;

/**
 * 投标
 */
public interface IBidService {
    List<Bid> getBidsByBidRequestId(Long bidRequestId);
}
