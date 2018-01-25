package com.yff.base.query;

public class BidRequestQueryObject extends QueryObject{

    private int bidRequestState = -1;
    //要查询的多个借款状态
    private int[] bidRequestStates;

    public int[] getBidRequestStates() {
        return bidRequestStates;
    }

    public void setBidRequestStates(int[] bidRequestStates) {
        this.bidRequestStates = bidRequestStates;
    }

    public int getBidRequestState() {
        return bidRequestState;
    }

    public void setBidRequestState(int bidRequestState) {
        this.bidRequestState = bidRequestState;
    }
}
