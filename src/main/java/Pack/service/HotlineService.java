package Pack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Pack.mapper.HotlineMapper;
import Pack.vo.HotlineDTO;
import Pack.vo.HotlineEntity;
import Pack.vo.HotlineList;

@Service
public class HotlineService {
    @Autowired
    public HotlineMapper hotlineMapper;

	public List<HotlineEntity> getAllHotlines() {
		return hotlineMapper.getAllHotlines();
	}

	public HotlineEntity getHotlineByID(String hotline_id) {
		return hotlineMapper.selectByID(hotline_id);
	}

	public int insert(HotlineDTO hotlineDTO) {
		return hotlineMapper.insert(hotlineDTO);
	}

	public int deletes(HotlineList hotlineList) {
		return hotlineMapper.deletes(hotlineList);
	}

	public int confirms(HotlineList hotlineList) {
		return hotlineMapper.confirms(hotlineList);
	}

	public List<HotlineEntity> getHotlineByWriterID(String writer_id) {
		return hotlineMapper.selectByWriterID(writer_id);
	}

}
