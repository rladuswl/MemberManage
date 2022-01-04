package com.example.membermanage.controller;

import com.example.membermanage.entity.Member;
import com.example.membermanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 멤버 정보 모두 불러오기
    @GetMapping("/member/list")
    public String memberList(Model model) {

        List<Member> members = memberService.memberList();

        /**
         * 잔여일수 구하는 로직
         * 잔여일수는 마감일자 - 오늘일자 이다.
         * 즉 신청일자가 2021-11-27일이고 마감일자가 2021-11-29 이라면
         * 잔여일수는 2일이다.
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String todayString = now.format(formatter); // 오늘 날짜 String화

        // 끝나는 날 - 오늘의 날짜 == 구하고자 하는 남은날짜
        for(Member member: members) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String endDate = member.getEnd();
            String today = todayString;

            try {
                Date endDateDate = dateFormat.parse(endDate);
                Date todayDate = dateFormat.parse(today);

                long diffDay = (endDateDate.getTime() - todayDate.getTime()) / (24*60*60*1000);
                int lastDay = (int)diffDay;
                if(lastDay < 0) {
                    lastDay = 0; // 잔여일수가 0일 미만이라면 -x 일로 표기되는게 아니라 0일로 표기된다.
                }

                member.setLast(lastDay); // 잔여일수 삽입
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 잔여일수 0일 남으면 자동 회원 삭제
        for(Member member: members) {
            if(member.getLast() == 0) {
                memberService.memberDelete(member.getId());
            }
        }

        model.addAttribute("list", memberService.memberList());
        return "memberlist";
    }


    // 회원정보 등록
    @GetMapping("/member/write")
    public String memberWriteForm() {
        return "memberwrite";
    }


    // 회원정보 등록 처리
    @PostMapping("/member/writing")
    public String memberWriting(Member member, Model model) {

        // ** 계산 영역 추후에 수정 **

        // 신청일자 셋팅
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        member.setDay(dateString); // 신청일자 셋팅


        // 종료일자 셋팅
        Calendar cal = Calendar.getInstance(); // 날짜 계산을 위해 Calendar 추상클래스 선언 및 getInstance() 메서드 사용
        cal.setTime(date);
        cal.add(Calendar.DATE, member.getLast());
        String dateString2 = format.format(cal.getTime());
        member.setEnd(dateString2);


        /**
         * 할인율 셋팅
         * 브론즈    0%
         * 실버     5%
         * 골드     10%
         * 플래티넘  15%
         * 다이아   20%
         */

        String memberGrade = member.getGrade();  // 멤버의 등급을 받아옴
        double discountPriceDouble = 0; // 멤버로 할인된 가격

        switch (memberGrade) {
            case "브론즈" :
                discountPriceDouble = member.getMoney();
                break;
            case "실버" :
                discountPriceDouble = member.getMoney() * 0.95;
                break;
            case "골드" :
                discountPriceDouble = member.getMoney() * 0.9;
                break;
            case "플래티넘" :
                discountPriceDouble = member.getMoney() * 0.85;
                break;
            case "다이아" :
                discountPriceDouble = member.getMoney() * 0.8;
                break;
        }

        int discountPrice = (int)discountPriceDouble;
        member.setMoney(discountPrice);


        memberService.write(member);
        return "redirect:/member/list";
    }


    // 회원 상세 페이지
    @GetMapping("member/view") // localhost:8080/member/view?id=1
    public String boardView(Model model, Integer id) {
        model.addAttribute("member", memberService.memberView(id));
        return "memberview";
    }


    // 특정 게시물 삭제
    @GetMapping("/member/delete")
    public String memberDelete(Integer id) {
        memberService.memberDelete(id);
        return "redirect:/member/list";
    }


    // 게시글 수정 페이지
    @GetMapping("/member/modify/{id}")
    public String memberModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("member", memberService.memberView(id));
        return "membermodify";
    }


    // 게시글 수정 처리
    @PostMapping("/member/update/{id}")
    public String memberUpdate(@PathVariable("id") Integer id, Member member) {
        Member update = memberService.memberView(id);

        /**
         * 할인율 셋팅
         * 브론즈    0%
         * 실버     5%
         * 골드     10%
         * 플래티넘  15%
         * 다이아   20%
         */

        String memberGrade = member.getGrade();  // 멤버의 등급을 받아옴
        double discountPriceDouble = 0; // 멤버로 할인된 가격
        switch (memberGrade) {
            case "브론즈" :
                discountPriceDouble = member.getMoney();
                break;
            case "실버" :
                discountPriceDouble = member.getMoney() * 0.95;
                break;
            case "골드" :
                discountPriceDouble = member.getMoney() * 0.9;
                break;
            case "플래티넘" :
                discountPriceDouble = member.getMoney() * 0.85;
                break;
            case "다이아" :
                discountPriceDouble = member.getMoney() * 0.8;
                break;
        }
        int discountPrice = (int)discountPriceDouble;


        update.setGrade(member.getGrade());
        update.setName(member.getName());
        update.setType(member.getType());
        update.setMoney(discountPrice);
        update.setAddress(member.getAddress());
        update.setPhone(member.getPhone());


//        // 신청일자 설정
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String dateString = format.format(date);
//        update.setDay(dateString);
//        // 신청일자 셋팅 완료

        // 신청일자는 수정하면 안됨 (수정하면 수정한 날짜로 변경되기 때문)


        // 종료일자 설정
        Calendar cal = Calendar.getInstance(); // 날짜 계산을 위해 Calendar 추상클래스 선언 및 getInstance() 메서드 사용
        cal.setTime(date);
        cal.add(Calendar.DATE, member.getLast());
        String dateString2 = format.format(cal.getTime());
        update.setEnd(dateString2);
        // 종료일자 셋팅 완료

        memberService.write(update);
        return "redirect:/member/list";
    }



    // 멤버 기간 연장
    @GetMapping("/member/plus")
    public String memberPlus(Model model) {
        return "plus";
    }

    @PostMapping("/member/plusPro")
    public String memberPlusPro(int plusDate) {
        List<Member> members = memberService.memberList();

        for(Member member: members) {
            String endDay = member.getEnd(); // 멤버의 종료일 구하기
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal = Calendar.getInstance();
            Date regDate = null;
            try {
                regDate = format.parse(endDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(regDate);

            cal.add(Calendar.DATE, plusDate);
            String dateValue = format.format(cal.getTime());

            member.setEnd(dateValue);
            memberService.write(member);


            // 오류발생 데이터는 정상적이지만
            // setEnd(dateValue) 를 하면 업데이트가 안됨!
            // 오류 해결 -> memberService.write(member) 을 해줘야 저장됨
        }
        return "redirect:/member/list";
    }
}
