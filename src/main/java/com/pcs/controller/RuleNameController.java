package com.pcs.controller;

import com.pcs.model.RuleName;
import com.pcs.service.RuleNameService;
import com.pcs.web.dto.RuleNameDTO;
import com.pcs.web.mapper.RuleNameMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;
    @Autowired
    private RuleNameMapper ruleNameMapper;


    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleNameDTOs", ruleNameService.getRuleNameDTOs());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String showRuleNameAddForm(RuleNameDTO ruleNameDTO) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String addRuleName(@Valid RuleNameDTO ruleNameDTO, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            RuleName ruleName = ruleNameMapper.toRuleName(ruleNameDTO);
            ruleNameService.save(ruleName);
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showRuleNameUpdateForm(@PathVariable("id") Integer id, Model model) throws IllegalArgumentException {
        model.addAttribute(
                "ruleNameDTO", ruleNameMapper.toRuleNameDTO(ruleNameService.getById(id)));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameDTO ruleNameDTO,
                                 BindingResult result, Model model) throws IllegalArgumentException {
        if (!result.hasErrors()) {
            RuleName ruleName = ruleNameMapper.toRuleName(ruleNameDTO);
            ruleNameService.update(ruleName);
            return "redirect:/ruleName/list";
        }
        model.addAttribute("ruleNameDTO", ruleNameDTO);
        return "ruleName/update";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) throws IllegalArgumentException {
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException.getMessage();
    }
}

