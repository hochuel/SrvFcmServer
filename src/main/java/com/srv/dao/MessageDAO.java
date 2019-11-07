package com.srv.dao;

import com.srv.vo.TbMsgVO;

import java.util.List;

public interface MessageDAO {

    public List<TbMsgVO> selectTbMsg() throws Exception;

    public int insertTbMsg(TbMsgVO paramVO) throws Exception;

    public int deleteTbMsg(TbMsgVO paramVO) throws Exception;

    public int insertTbMsgSend(TbMsgVO paramVO) throws Exception;
}
