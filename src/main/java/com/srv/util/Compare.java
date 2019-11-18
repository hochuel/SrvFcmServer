package com.srv.util;

import com.srv.vo.TbMsgVO;

import java.util.ArrayList;
import java.util.List;

public class Compare {
    private final int resultCnt = 100;
    private List<TbMsgVO> resultList = null;
    private List<TbMsgVO> dataList = null;


    public List<TbMsgVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<TbMsgVO> resultList) {
        this.resultList = resultList;
    }

    public List<TbMsgVO> getDataList() {
        return dataList;
    }

    public void setDataList(List<TbMsgVO> dataList) {
        this.dataList = dataList;
    }

    public void compare(TbMsgVO vo){
        boolean start = true;
        int index = 0;

        resultList = null;

        while(start){

            TbMsgVO dataVO = (TbMsgVO)dataList.get(index);
            String appId = dataVO.getAppId();

            if(appId.equals(vo.getAppId())){
                if(resultList == null || resultList.size() >= resultCnt){
                    resultList = new ArrayList<TbMsgVO>();
                }

                resultList.add(dataVO);
            }
            index++;

            if(resultList.size() >= resultCnt || index >= dataList.size()){
                start = false;
            }
        }

        for(TbMsgVO delVo : resultList){
            dataList.remove(delVo);
        }
    }
}
