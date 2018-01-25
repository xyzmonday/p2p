package com.yff.base.service.impl;

import com.yff.base.domain.Bid;
import com.yff.base.mapper.BidMapper;
import com.yff.base.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投标
 */
@Service
public class BidServiceImpl implements IBidService {

    @Autowired
    private BidMapper bidMapper;

    @Override
    public List<Bid> getBidsByBidRequestId(Long bidRequestId) {
        return null;
    }
}
