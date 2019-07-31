package bjfu.lixian.mapper;



import bjfu.lixian.pojo.Comments;
import bjfu.lixian.pojo.vo.CommentsVO;
import bjfu.lixian.utils.MyMapper;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}