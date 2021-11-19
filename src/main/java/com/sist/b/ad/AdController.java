package com.sist.b.ad;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ad/**")
public class AdController {
	
	@Autowired
	private AdService adService;
	
	@GetMapping("list")
	public ModelAndView getList(ModelAndView mv) throws Exception {
		List<AdVO> ar = adService.getList();
		mv.addObject("adVOs", ar);
		mv.setViewName("ad/list");
		return mv;
	}
	
	@GetMapping("select")
	public ModelAndView getOne(ModelAndView mv, AdVO adVO) throws Exception {
		adVO = adService.getOne(adVO);
		mv.addObject("adVO", adVO);
		mv.setViewName("ad/select");
		return mv;
	}
	
	@GetMapping("create")
	public String setInsert(@ModelAttribute AdVO adVO) throws Exception {
		return "ad/insert";
	}
	
	@PostMapping("create")
	public String setInsert(@Valid AdVO adVO, BindingResult bindingResult, MultipartFile file) throws Exception {
		
		if(bindingResult.hasErrors()) {
			return "board/insert";
		}
		
		int result = adService.setInsert(adVO, file);
		return "redirect:./list";
	}
	
	@GetMapping("delete")
	public String setDelete(AdVO adVO) throws Exception {
		int result = adService.setDelete(adVO);
		return "redirect:./list";
	}
}