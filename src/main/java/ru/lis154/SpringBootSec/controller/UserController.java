package ru.lis154.SpringBootSec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.lis154.SpringBootSec.model.User;
import ru.lis154.SpringBootSec.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {
    private int page;
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView allUser(@RequestParam(defaultValue = "1") int page) {
        List<User> listUser = userService.allUser(page);
        int userCount = userService.userCount();
        int pageCout = (userCount + 9) / 10;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameAdmin = auth.getName();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allUser");
        modelAndView.addObject("userList", listUser);
        modelAndView.addObject("usersCount", userCount);
        modelAndView.addObject("pagesCount", pageCout);
        modelAndView.addObject("username", nameAdmin);

        this.page = page;
        return modelAndView;
    }

    @RequestMapping(value = "/admin/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") int id) {
        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute("user") User user, @RequestParam("role") String role) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/?page=" + this.page);
        user.setRolesOnForm(role);
        userService.edit(user);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.GET)
    public ModelAndView addPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute("user") User user, @RequestParam("role") String role) {


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/?page=" + this.page);
        System.out.println("ru.lis154.SpringbootSec_________" + role);
        user.setRolesOnForm(role);
        userService.add(user);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") int id) {
        // User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/?page=" + this.page);
        userService.delete(id);
        return modelAndView;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView OneUser(@RequestParam("username") String name) {

        System.out.println(name);
        User listUser = (User) userService.loadUserByUsername(name);
        System.out.println(listUser);
        //int userCount = userService.userCount();
        //int pageCout = (userCount + 9)/10;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("OneUser");
        modelAndView.addObject("user1", listUser);
        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }


}
