package Pack.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder
public class HotlineEntity {
	String hotline_id;
    String category;
    String title;
    String writer;
    String writer_id;
    String content;
    String reg_date;
    String confirm_date;
    String status;
}