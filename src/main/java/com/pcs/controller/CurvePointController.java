package com.pcs.controller;

import com.pcs.configuration.ConnectedUser;
import com.pcs.model.CurvePoint;
import com.pcs.service.CurvePointService;
import com.pcs.web.dto.CurvePointDTO;
import com.pcs.web.mapper.CurvePointMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Controller
public class CurvePointController {

    @Autowired
    private CurvePointService curvePointService;
    @Autowired
    private CurvePointMapper curvePointMapper;
    @Autowired
    private ConnectedUser connectedUser;

    @RequestMapping("/curvePoint/list")
    public String home(Model model, UsernamePasswordAuthenticationToken token) {
        model.addAttribute("curvePointDTOs", curvePointMapper.getCurvePointDTOs());
        model.addAttribute("connectedUserName", connectedUser.getUsernamePasswordLoginInfo(token));
        model.addAttribute("hasRoleAdmin", connectedUser.hasRole(token, "ROLE_ADMIN"));
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String showCurvePointAddForm(CurvePointDTO curvePointDTO) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String addCurvePoint(@Valid CurvePointDTO curvePointDTO, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            CurvePoint curvePoint = curvePointMapper.toCurvePoint(curvePointDTO);
            curvePoint.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            curvePointService.save(curvePoint);
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showCurvePointUpdateForm(@PathVariable("id") Integer id, Model model) throws IllegalArgumentException {
        model.addAttribute(
                "curvePointDTO", curvePointMapper.toCurvePointDTO(curvePointService.getById(id)));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePointDTO curvePointDTO,
                                   BindingResult result, Model model) throws IllegalArgumentException {
        if (!result.hasErrors()) {
            CurvePoint curvePoint = curvePointMapper.toCurvePoint(curvePointDTO);
            curvePointService.update(curvePoint);
            return "redirect:/curvePoint/list";
        }
        model.addAttribute("CurvePointDTO", curvePointDTO);
        return "curvePoint/update";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) throws IllegalArgumentException {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException.getMessage();
    }

}
