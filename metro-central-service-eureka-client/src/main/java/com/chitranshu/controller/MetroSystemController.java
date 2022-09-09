package com.chitranshu.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.Transaction;
import com.chitranshu.model.service.MetroSystemService;

@Controller
public class MetroSystemController {
	
	@Autowired
	MetroSystemService metroServiceImpl;
	
	@ModelAttribute("allStations")
	public List<String> getAllStations(){
		return metroServiceImpl.getStationNamesList().getAllStationNames();
	}
	
	@RequestMapping("/swipeIn")
	public ModelAndView swipeInController() {
		return new ModelAndView("SwipeInInput");
	}
	
	@RequestMapping("/index")
	public ModelAndView indexController() {
		return new ModelAndView("Index");
	}
	
	@RequestMapping("/swipedInIndex")
	public ModelAndView swipedInIndexController() {
		return new ModelAndView("SwipedInIndex");
	}
	
	
	@RequestMapping("/swipeIn/Input")
	public ModelAndView swipeInInputController(@RequestParam("stationName") String stationName, HttpSession session) {
		ModelAndView modelAndView=new ModelAndView();
		MetroCard card=(MetroCard)session.getAttribute("card");
		if(metroServiceImpl.swipeIn(card, stationName)) {
			modelAndView.addObject("message","Swiped In successfully at "+stationName);
			modelAndView.setViewName("SwipedInIndex");
		}
		else {
			modelAndView.addObject("message","Swipe In failed! Please try again...");
			modelAndView.setViewName("Index");
		}
		return modelAndView;
	}
	
	@RequestMapping("/swipeOut")
	public ModelAndView swipeOutController() {
		return new ModelAndView("SwipeOutInput");
	}
	
	@RequestMapping("/swipeOut/Input")
	public ModelAndView swipeOutInputController(@RequestParam("stationName") String stationName, HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		String message=metroServiceImpl.swipeOut(card, stationName);
		MetroCard updatedCard=metroServiceImpl.getCardById(card.getCardId());
		session.setAttribute("card", updatedCard);
		return new ModelAndView("Display","message", message);
	}	
	
//	@RequestMapping("/checkBalance")
//	public ModelAndView checkBalanceController(HttpSession session) {
//		MetroCard card=(MetroCard)session.getAttribute("card");
//		double bal=metroServiceImpl.cardBalance(card.getCardId());
//		String message="Available balance : "+bal;
//		return new ModelAndView("Display","message",message);
//	}
	
	@RequestMapping("/rechargeCard")
	public ModelAndView rechargeCardController() {
		return new ModelAndView("RechargeCard");
	}
	@RequestMapping("/rechargeCard/update")
	public ModelAndView validateAmountAndRechargeCardController(@RequestParam double money,HttpSession session) {
		if(money>0) {
			MetroCard card=(MetroCard)session.getAttribute("card");
			if(metroServiceImpl.rechargeCard(card,money)) {
				String message="Rs."+money+" added successfully!";
				MetroCard updatedCard=metroServiceImpl.getCardById(card.getCardId());
				session.setAttribute("card", updatedCard);
				return new ModelAndView("Display","message", message);
			}
			return new ModelAndView("RechargeCard","rechargeCard", "Recharge Failed! Server down... please try again after some time");
		}
		return new ModelAndView("RechargeCard","rechargeCard", "Recharge Failed! Please enter a positive amount...");
	}
	
	@RequestMapping("/showAllPreviousTransactions")
	public ModelAndView slowAllTransactionsController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		List<Transaction> history=metroServiceImpl.transactionHistory(card.getCardId()).getTransactionList();
		if(history==null || history.isEmpty()) {
			return new ModelAndView("Display","message", "No previous transactions to show");
		}
		return new ModelAndView("ShowAllTransactions","allTransactions", history);
	}
	
}
