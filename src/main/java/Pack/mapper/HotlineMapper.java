package Pack.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import Pack.vo.HotlineDTO;
import Pack.vo.HotlineEntity;
import Pack.vo.HotlineList;


@Repository
@Mapper
public interface HotlineMapper {

	List<HotlineEntity> getAllHotlines();

	HotlineEntity selectByID(String hotline_id);
    
	int insert(HotlineDTO hotlineDTO);
	
	int deletes(HotlineList hotlineList);
	
	int confirms(HotlineList hotlineList);

	List<HotlineEntity> selectByWriterID(String writer_id);
}
