package Pack.vo;

import java.util.List;

import lombok.*;

@ToString 
@Getter
@Setter
public class HotlineList {
	List<HotlineInfo> hotlineInfoList;
	String status;
}
