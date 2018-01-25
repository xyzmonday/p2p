package com.yff.base.service;

import com.yff.base.domain.BidRequestAuditHistory;

import java.util.List;

public interface IBidRequestHistoryService {

    List<BidRequestAuditHistory> getHistorysByBidRequestId(Long bidRequestId);
}
