package com.srv.dao;

import com.srv.vo.TbMsgVO;

import java.util.List;
import java.util.Map;

public interface MessageDAO {

    public List<TbMsgVO> selectTbMsg(TbMsgVO vo) throws Exception;

    public int updateTbMsgPolKey(TbMsgVO vo) throws Exception;

    public int insertTbMsg(Map map) throws Exception;

    public int updateTbMsg(Map map) throws Exception;

    public int deleteTbMsg(TbMsgVO vo) throws Exception;

    public int insertTbMsgSend(Map map) throws Exception;

    public int updateTbMsgSend(Map map) throws Exception;
}
