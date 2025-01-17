package com.sist.b.chat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sist.b.chat.chatroom.ChatRoomJoinService;
import com.sist.b.chat.chatroom.ChatRoomJoinVO;
import com.sist.b.follow.FollowService;
import com.sist.b.user.UserService;
import com.sist.b.user.UserVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/chat/**")
public class ChatController {
	
	@Autowired
	private ChatRoomJoinService chatRoomJoinService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FollowService followService;
	
	private final SimpMessagingTemplate simpMessagingTemplate;

	
	@GetMapping("/chat/getFollowUser")
	public ModelAndView getFollowUser(HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 
		
	    List<UserVO> list = followService.myFollowList(userVO);
	    
	    mv.addObject("gbn", "follow");
	    mv.addObject("searchUserList", list);
	    mv.setViewName("chat/searchUserList");
		return mv;
	}
	 
	
	/************** 채팅방 모달창 유저 검색 *****************/
	@GetMapping("/chat/getSearchUser")
	public ModelAndView getSearchUser(HttpSession session, String searchText) throws Exception {
		ModelAndView mv = new ModelAndView();	
		
		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 

//		System.out.println("searchText:" + searchText);
		List<UserVO> list = userService.getSaerchUser(userVO, searchText);
		
		System.out.println("list.size:"+ list.size());
		
		mv.addObject("gbn", "search");
		mv.addObject("searchUserList", list);
		mv.setViewName("chat/searchUserList");
		return mv;
	}
	/****************************************************/
	
	
	
	/************** 채팅 로그 가져오기 *****************/
	@PostMapping("/chat/getChatMessage")
	@ResponseBody
	public List<ChatMessageVO> getChatMessage(ChatRoomJoinVO chatRoomJoinVO, HttpSession session) throws Exception {
	    List<ChatMessageVO> messageList = chatRoomJoinService.getChatMessage(chatRoomJoinVO);

	    return messageList;
	}
	/****************************************************/
	
	@PostMapping("/chat/getDetailInfo")
	public ModelAndView getDetailInfo(UserVO userVO) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		userVO = userService.getUserInfo(userVO.getUserNum());
		
		System.out.println("saddss::"+userVO.getUsername());
		System.out.println("saddss::"+userVO.getUserNum());
		
		mv.addObject("receiverUserVO", userVO);
		mv.setViewName("chat/chatDetailInfo");
		return mv;
	}
	
	
	/***************************** 채팅방 *****************************/
	@RequestMapping("/chat/t/{chatRoomNum}")
	public ModelAndView goChat(@PathVariable("chatRoomNum") Long chatRoomNum, 
			HttpSession session, ChatRoomJoinVO chatRoomJoinVO, String receiverUserId,
			HttpServletRequest request) throws Exception {
		System.out.println(chatRoomNum+"chatRoom접속");
		
		ModelAndView mv = new ModelAndView();
		
		// GET으로 들어올 경우 페이지 리다이렉트
		if (request.getHeader("REFERER") == null) {			
			mv.setViewName("redirect:../inbox");
			return mv;
		}
	

		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 
	    
	    //채팅 상대 정보
	    UserVO receiverUserVO = userService.getUserInfo(chatRoomJoinVO.getUserNum());
	    
	    
	    mv.addObject("roomNum", chatRoomJoinVO.getRoomNum());
	    mv.addObject("userVO", userVO);
	    mv.addObject("receiverUserVO", receiverUserVO);
	    mv.setViewName("chat/chatForm");
	    
		return mv;
	}
	/* ******************************************************************* */
	
	

	/***************************** 채팅방 생성 *****************************/
	@GetMapping("/chat/newChat")
	@ResponseBody
	public Long getChatRoom(HttpSession session, ChatRoomJoinVO chatRoomJoinVO, Long myUserNum, RedirectAttributes rttr) throws Exception {
		
		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 
		
//	    System.out.println("userNum:"+chatRoomJoinVO.getUserNum());
	    
		Long chatRoomNum = chatRoomJoinService.newChatRoom(chatRoomJoinVO, userVO.getUserNum());
		
		rttr.addFlashAttribute("chatRoomJoinVO", chatRoomJoinVO);
		
		return chatRoomNum;
//		return "redirect:/chat/t/"+chatRoomNum;
	}
	/* *************************************************************** */

	
	/* 채팅하고있는 유저 리스트 가져옴 */
	@GetMapping("/chat/getChatUserList")
	public ModelAndView getChatUserList(HttpSession session) throws Exception {
		System.out.println("getChatUserList");
		ModelAndView mv = new ModelAndView();
		
		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 

	    
		List<ChatRoomJoinVO> ar = chatRoomJoinService.getChatUserList(userVO);
		
		
		
		mv.addObject("list", ar);
		mv.setViewName("chat/chatUserList");
		
		return mv;
	}
	
	
	/* 채팅 퇴장 */
	@PostMapping("/chat/setRemoveChat")
	@ResponseBody
	public int setRemoveChat(ChatRoomJoinVO chatRoomJoinVO) throws Exception {
		return chatRoomJoinService.setUpdateChatRoomJoin(chatRoomJoinVO);
	}
	
	
	/* 채팅 첫 화면 */
	@GetMapping("/chat/inbox")
	public ModelAndView chat(HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		Object object = session.getAttribute("SPRING_SECURITY_CONTEXT");
	    SecurityContextImpl sc = (SecurityContextImpl)object;
	    Authentication authentication = sc.getAuthentication();
	    UserVO userVO = (UserVO)authentication.getPrincipal(); 
		
		mv.addObject("userVO", userVO);
		mv.setViewName("chat/chat");
		
		return mv;
	}
	
	
	/* 채팅 메시지 send */
	@MessageMapping("/chat")
	public void sendMessage(ChatMessageVO chatMessageVO) throws Exception {
		//System.out.println("nickname: " + chatMessageVO.getUserNickName() + ", contents: " + chatMessageVO.getContents());

		
		System.out.println("send Chat ~ chatMessagVO:"+chatMessageVO);
		
		chatRoomJoinService.setChatMessage(chatMessageVO);
		simpMessagingTemplate.convertAndSend("/topic/"+chatMessageVO.getReceiverNum(), chatMessageVO);
		
		ChatRoomJoinVO chatRoomJoinVO = new ChatRoomJoinVO();
		chatRoomJoinVO.setRoomNum(chatMessageVO.getRoomNum());
		chatRoomJoinVO.setUserNum(chatMessageVO.getReceiverNum());
		//채팅받는 상대 exitYN = N으로
		chatRoomJoinService.setUpdateReChatRoomJoin(chatRoomJoinVO); 
	}
	
	
}
