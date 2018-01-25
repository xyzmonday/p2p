package com.yff.base.service.impl;

import com.yff.base.domain.BidRequestAuditHistory;
import com.yff.base.mapper.BidRequestAuditHistoryMapper;
import com.yff.base.service.IBidRequestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 借款审核历史
 */
@Service
public class BidRequestHistoryServiceImpl implements IBidRequestHistoryService {

    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;


    @Override
    public List<BidRequestAuditHistory> getHistorysByBidRequestId(Long bidRequestId) {
        return bidRequestAuditHistoryMapper.selectAllByBidRequestId(bidRequestId);
    }
}
