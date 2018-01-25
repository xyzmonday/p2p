package com.yff.base.mapper;

import com.yff.base.domain.BidRequest;
import com.yff.base.query.BidRequestQueryObject;

import java.util.List;

public interface BidRequestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BidRequest record);

    int insertSelective(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BidRequest record);

    int updateByPrimaryKey(BidRequest record);

    List<BidRequest> queryByPageCondition(BidRequestQueryObject qo);
}