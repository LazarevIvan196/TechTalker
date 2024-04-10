package com.example.techtalker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LanguageController {
    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam("language") String lang, HttpServletRequest request, HttpServletResponse response) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lang));

        String referrer = request.getHeader("Referer");
        referrer = (referrer == null || referrer.isEmpty()) ? "/" : referrer;
        return "redirect:" + referrer;
    }
}
