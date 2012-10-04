package com.d2fn.jester.plugin.zerocater;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import java.util.List;

public class ZeroCaterPlugin implements Plugin {
    private final String username;
    private final String password;

    public ZeroCaterPlugin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getName() {
        return "zerocater";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if ("lunch".equals(msg.getMessage())) {
            bot.sendMessage(msg.getChannel(), msg.getSender() + ": Checking lunch for today....");
            final WebClient webClient = new WebClient(BrowserVersion.CHROME_16);
            try {
                final HtmlPage page = webClient.getPage("http://www.zerocater.com/accounts/login/");
                final HtmlForm form = page.getForms().get(0);
                form.getInputByName("username").setValueAttribute(username);
                form.getInputByName("password").setValueAttribute(password);
                final HtmlSubmitInput input = form.getInputByValue("Sign in");

                final HtmlPage page2 = input.click();
                System.out.println("page2.getTitleText() = " + page2.getTitleText());

                final List<?> items = page2.getByXPath("//span[@class='meal-is-today']/../../div[@class='vendor-name']");
                for (Object item : items) {
                    if (item instanceof HtmlDivision) {
                        HtmlDivision div = (HtmlDivision)item;
                        String vendorName = div.getTextContent();
                        bot.sendMessage(msg.getChannel(), msg.getSender() + ": Lunch today comes from " + vendorName);
                        return;
                    }
                }

                bot.sendMessage(msg.getSender(), "Couldn't find lunch");
            } finally {
                webClient.closeAllWindows();
            }

        }
    }
}
