package com.yff.base.mapper;

import com.yff.base.domain.BidRequestAuditHistory;

import java.util.List;

public interface BidRequestAuditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BidRequestAuditHistory record);

    int insertSelective(BidRequestAuditHistory record);

    BidRequestAuditHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BidRequestAuditHistory record);

    int updateByPrimaryKey(BidRequestAuditHistory record);

    List<BidRequestAuditHistory> selectAllByBidRequestId(Long bidRequestId);
}