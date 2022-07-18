package Pack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Pack.service.HotlineService;
import Pack.service.PushNotificationService;
import Pack.service.TestService;
import Pack.vo.CommResponse;
import Pack.vo.HotlineDTO;
import Pack.vo.HotlineEntity;
import Pack.vo.HotlineInfo;
import Pack.vo.HotlineList;
import Pack.vo.TestVo;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
@Slf4j
public class MainController {
	@Autowired
	TestService testService;
	@Autowired
	HotlineService hotlineService;
	
	@GetMapping("/test")
	public List<TestVo> test() {
		System.out.println(111);
	    List<TestVo> testList = testService.selectTest();
	    System.out.println(testList);
	    return testList;
	}
	
	@GetMapping("/")
	public List<HotlineEntity> getAllHotlines() {
		System.out.println("select All");
		return hotlineService.getAllHotlines();
	}
	
	@GetMapping("/id/{id}")
	public HotlineEntity getHotlineByID(@PathVariable String id) {
		System.out.println("Select By id "+ id);
		return hotlineService.getHotlineByID(id);
	}
	
	@GetMapping("/writerid/{writer_id}")
	public List<HotlineEntity> getHotlineByWriterID(@PathVariable String writer_id) {
		System.out.println("Select By id "+ writer_id);
		return hotlineService.getHotlineByWriterID(writer_id);
	}
	
	@PostMapping("/")
	public Boolean insertHotline(@RequestBody HotlineDTO hotlineDTO) {
		System.out.println("insert Hotline");
		System.out.println(hotlineDTO);
		int result = hotlineService.insert(hotlineDTO);
		return result > 0 ? true : false;
	}
	
	@DeleteMapping("/")
	public Boolean getHotlineByID(@RequestBody HotlineList hotlineList) {
		System.out.println("deletes Hotline");
		System.out.println(hotlineList);
		int result = hotlineService.deletes(hotlineList);
		return result > 0 ? true : false;
	}
	
	@PutMapping("/confirms")
	public Boolean hotlineConfirm(@RequestBody HotlineList hotlineList) {
		System.out.println("confirms Hotline");
		System.out.println(hotlineList);
		int result = hotlineService.confirms(hotlineList); 
		if (result > 0) {
			for (HotlineInfo hotlineInfo : hotlineList.getHotlineInfoList()) {
				sendFCM("HOT LINE", "작성하신 글이 " +hotlineList.getStatus() + "되었습니다.", hotlineInfo.getWriter_id());
			} 
		}
		return result > 0 ? true : false;
	}
	
	@PostMapping("/fcm")
    public ResponseEntity<?> reqFcm(@RequestParam(required = true) String title, @RequestParam(required = true) String body, @RequestParam(required = true) String topic) {
        log.info("** title : {}",title);
        log.info("** body : {}",body);

        CommResponse res = new CommResponse();
        try {
            PushNotificationService.sendCommonMessage(title, body, topic);
            res.setCdResult("200");
        } catch(Exception e) {
            log.error(e.getMessage());
            res.setCdResult("500");
            res.setMsgResult("처리중 에러 발생");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
	
	@GetMapping("/send/type/{type}/topic/{topic}")
    public ResponseEntity<?> reqFcm(@PathVariable String type, @PathVariable String topic) {
		String title = type + " 지시 등록";
		String body = topic + "창고에 새로운 " + type + " 지시가 등록되었습니다.";
        log.info("** title : {}",title);
        log.info("** body : {}", body);

        CommResponse res = new CommResponse();
        try {
            PushNotificationService.sendCommonMessage(title, body, topic);
            res.setCdResult("200");
        } catch(Exception e) {
            log.error(e.getMessage());
            res.setCdResult("500");
            res.setMsgResult("처리중 에러 발생");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
	
    public ResponseEntity<?> sendFCM(String title, String body, String topic) {
        log.info("** title : {}",title);
        log.info("** body : {}",body);

        CommResponse res = new CommResponse();
        try {
            PushNotificationService.sendCommonMessage(title, body, topic);
            res.setCdResult("200");
        } catch(Exception e) {
            log.error(e.getMessage());
            res.setCdResult("500");
            res.setMsgResult("처리중 에러 발생");
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(res);
    }
}
