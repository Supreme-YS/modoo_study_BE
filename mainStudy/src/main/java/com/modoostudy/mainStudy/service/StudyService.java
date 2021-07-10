package com.modoostudy.mainStudy.service;

import com.modoostudy.mainStudy.dto.StudyDto;
import com.modoostudy.mainStudy.dto.StudyGuestDto;
import com.modoostudy.mainStudy.dto.StudyInterestDto;
import com.modoostudy.mainStudy.dto.function.CreateStudyDto;
import com.modoostudy.mainStudy.dto.function.LoginUserDto;
import com.modoostudy.mainStudy.dto.function.StudyFormDto;
import com.modoostudy.mainStudy.entity.MappingStudyGuest;
import com.modoostudy.mainStudy.entity.MappingStudyInterest;
import com.modoostudy.mainStudy.entity.Study;
import com.modoostudy.mainStudy.entity.User;
import com.modoostudy.mainStudy.mapper.*;
import com.modoostudy.mainStudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyService {

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final RegionRepository regionRepository;
    private final StudyRepository studyRepository;
    private final MappingStudyInterestRepository mappingStudyInterestRepository;
    private final MappingStudyGuestRepository mappingStudyGuestRepository;

//    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("name");
//    private final EntityManager em = emf.createEntityManager();


    /**
     * JWT 토큰으로부터 로그인 유저 정보 가져오기
     */
    public LoginUserDto getUserFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByGEmail(authentication.getName());

        return LoginUserDto.builder()
                .userID(user.getUserID())
                .GEmail(user.getGEmail())
                .nickName(user.getNickname())
                .build();
    }


    /**
     * 스터디생성 폼 가져오기
     */

    public StudyFormDto getStudyForm() {

        return StudyFormDto.builder()
                .loginUserDto(getUserFromJWT())
                .interestDto(InterestMapper.INSTANCE.toDto(interestRepository.findAll()))
                .regionDto(RegionMapper.INSTANCE.toDto(regionRepository.findAll()))
                .build();
    }

    /**
     * 스터디 생성하기
     */
    public void createStudy(CreateStudyDto createStudyDto) {

        System.out.println(createStudyDto);
        System.out.println(createStudyDto.getRegistStudyInterestList());

        /*
        studyTB 입력
        */
        Study createStudyEntity = StudyMapper.INSTANCE.toEntity(
                StudyDto.builder()
                        .hostID(getUserFromJWT().getUserID())
                        .title(createStudyDto.getRegistStudy().getTitle())
                        .periodStart(createStudyDto.getRegistStudy().getPeriodStart())
                        .periodEnd(createStudyDto.getRegistStudy().getPeriodEnd())
                        .goal(createStudyDto.getRegistStudy().getGoal())
                        .need(createStudyDto.getRegistStudy().getNeed())
                        .onoffline(createStudyDto.getRegistStudy().getOnoffline())
                        .details(createStudyDto.getRegistStudy().getDetails())
                        .build());

        studyRepository.save(createStudyEntity);


        /*
        mappingStudyInterestTB 입력
         */
        Long registStudyID = createStudyEntity.getStudyID();

        for (StudyInterestDto.registStudyInterest interest : createStudyDto.getRegistStudyInterestList())
        {
            mappingStudyInterestRepository.save(
                    StudyInterestMapper.INSTANCE.toEntity(
                            StudyInterestDto.builder()
                                    .studyID(registStudyID)
                                    .interestID(interest.getInterestID())
                                    .build()));

        }
    }

    /**
     * 메인페이지 스터디 리스트 받아오기
     */

    /**
     * 스터디페이지 상세보기
     */
    public void readStudyDetail(Long studyID){

        System.out.println(studyRepository.findByStudyID(studyID));
        System.out.println(studyRepository.findByStudyID(studyID).getStudyID());
        System.out.println(studyRepository.findByStudyID(studyID).getStudyInterests());
        for (MappingStudyInterest mappingStudyInterestList : studyRepository.findByStudyID(studyID).getStudyInterests()){
            System.out.println(mappingStudyInterestList.getInterest().getInterestName());
        }








    }

    /**
     * 스터디 신청하기
     */
    public void applyStudyMember(Long studyID){
        MappingStudyGuest mappingStudyGuest = StudyGuestMapper.INSTANCE.toEntity(
                StudyGuestDto.builder()
                .guestID(getUserFromJWT().getUserID())
                .studyID(studyID)
                .status("대기")
                .build());

        mappingStudyGuestRepository.save(mappingStudyGuest);
    }





}
