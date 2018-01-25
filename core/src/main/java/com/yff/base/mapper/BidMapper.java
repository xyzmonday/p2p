package com.yff.base.mapper;

import com.yff.base.domain.Bid;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bid record);

    int insertSelective(Bid record);

    Bid selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bid record);

    int updateByPrimaryKey(Bid record);

    List<Bid> selectByBidRequestId(Long bidRequestId);

    /**
     * 设置标的状态
     *
     * @param bidRequestId
     * @param state
     */
    void updateBidState(@Param("bidRequestId") Long bidRequestId, @Param("state") Byte state);
}