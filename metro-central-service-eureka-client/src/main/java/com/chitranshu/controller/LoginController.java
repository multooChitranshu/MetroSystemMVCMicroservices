package com.chitranshu.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;
import com.chitranshu.bean.Transaction;
import com.chitranshu.model.service.MetroSystemService;

@Controller
public class LoginController {

	@Autowired
	MetroSystemService metroSystemServiceImpl;
	
	@ModelAttribute("allCardIds")
	public List<Long> getAllCards(){
		return metroSystemServiceImpl.getAllCardIds().getCardIds();
	}
	
	@RequestMapping("/")
	public ModelAndView loginCardController() {
		return new ModelAndView("Home");
	}
	
	@RequestMapping("/register")
	public ModelAndView newUserRegistrationController() {
		return new ModelAndView("Register", "cardCredentials", new MetroCardCredentials());
	}
	
	@RequestMapping("/saveCard")
	public ModelAndView registerNewCardController(@ModelAttribute("cardCredentials") MetroCardCredentials cardCredentials, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		String message="";
		long cardId=cardCredentials.getCardId();
//		System.out.println("\n*******"+cardId);
//		System.out.println("$$$$$"+metroSystemServiceImpl.getCardById(cardId));
		if(metroSystemServiceImpl.getCardById(cardId).getCardId()!=0) {
			message="Failed! This Aadhar ID "+cardId+" is already registered! Please try again, or go back to login";
		}
		else {
			MetroCard card=metroSystemServiceImpl.registerCard(cardCredentials);
			if(card.getCardId()==0) {
				message="Invalid credentials! Please try again...";
			}
			else {
			session.setAttribute("card", card);
			modelAndView.setViewName("Index");
			return modelAndView;
			}
		}
		modelAndView.addObject("message", message);
		modelAndView.setViewName("Register");
		return modelAndView;
	}
	
	@RequestMapping("/login")
	public ModelAndView existingCardController() {
		return new ModelAndView("Login", "cardCredentials", new MetroCardCredentials());
	}
	
	@RequestMapping("/loggedInCard")
	public ModelAndView loginExistingCardController(@ModelAttribute("cardCredentials") MetroCardCredentials cardCredentials, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		String message="";
		long cardId=cardCredentials.getCardId();
		if(metroSystemServiceImpl.getCardById(cardId).getCardId()==0) {
			message="Failed! Card ID "+cardId+" does not exist! Please try again... ";
		}
		else {
		MetroCard card=metroSystemServiceImpl.loginCard(cardCredentials);
			if(card.getCardId()!=0) {
				Transaction lastTransaction=metroSystemServiceImpl.lastTransaction(card.getCardId());
				if(lastTransaction==null || lastTransaction.getDateAndTimeOfExit()!=null) {
					modelAndView.setViewName("Index");
				}
				else if(lastTransaction.getDateAndTimeOfExit()==null) {
					modelAndView.setViewName("SwipedInIndex");
				}
				else {
					modelAndView.setViewName("Index");
				}
				session.setAttribute("card", card);
				return modelAndView;
			}
			message="Invalid Credentials! Please try again...";
		}
		modelAndView.addObject("message", message);
		modelAndView.addObject("cardCredentials", new MetroCardCredentials());
		modelAndView.setViewName("Login");
		return modelAndView;
	}
	
	@RequestMapping("/menuDecider")
	public ModelAndView menuDeciderController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		long cardId=card.getCardId();
		ModelAndView modelAndView = new ModelAndView();
		Transaction lastTransaction=metroSystemServiceImpl.lastTransaction(cardId);
		if(lastTransaction==null || lastTransaction.getDateAndTimeOfExit()!=null) {
			modelAndView.setViewName("Index");
		}
		else if(lastTransaction.getDateAndTimeOfExit()==null) {
			modelAndView.setViewName("SwipedInIndex");
		}
		else {
			modelAndView.setViewName("Index");
		}
		return modelAndView;
		
	}
	
	@RequestMapping("/logout")
	public ModelAndView logoutController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		String message="Card ID: "+card.getCardId()+" logged out successfully!";
		session.invalidate();
		return new ModelAndView("Home","message",message);
	}
	
}
