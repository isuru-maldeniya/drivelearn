package com.example.drivelearnbackend.Sevices;

import com.example.drivelearnbackend.Controllers.DTO.SessionDTO;
import com.example.drivelearnbackend.Controllers.DTO.StudentDTO;
import com.example.drivelearnbackend.Repositories.*;
import com.example.drivelearnbackend.Repositories.Entity.*;
import com.example.drivelearnbackend.Sevices.Support.VehiclesTypeEn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserReceiveNotificationRepository  userReceiveNotificationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StuSessionRepository stuSessionRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;


    private User giveUser(int id,String username,int userType){
        User user = null;
        int count=0;
        LinkedList<User> userList;
        if(id==0){
            userList=userRepository.findByUsernameAndAndUserType(username, userType);
            for (User user1 : userList) {
                user = user1;
            }
        }else{
            userList=userRepository.findByExternalIdAndUserType(id,userType);
            for (User user1 : userList) {
                user = user1;
            }
            System.out.println(user.getUsername());
        }
        return user;
    }

    private VechileType giveVehicleType(String name){
        if(name=="car_auto"){
            return vehicleTypeRepository.findById(1).get();
        }else if(name=="wheeler"){
            return vehicleTypeRepository.findById(2).get();
        }else if(name=="bike"){
            return vehicleTypeRepository.findById(3).get();
        }else if(name=="heavy"){
            return vehicleTypeRepository.findById(4).get();
        }else if(name=="car_manual"){
            return vehicleTypeRepository.findById(6).get();
        }else {
            return vehicleTypeRepository.findById(5).get();
        }
    }

    public void addSession(SessionDTO dto){
        Employee manager=employeeRepository.findById(dto.getManagerId()).get();
        Employee trainer=employeeRepository.findById(dto.getTrainerId()).get();

        sessionRepository.save(new Session(dto.getDate(),1, dto.getNumOfStudent(), dto.getRoute(), dto.getStartTime(), dto.getEndTime(), trainer,manager, manager.getBranch(),giveVehicleType(dto.getVehicleType())));

        User TrainerUser=giveUser(dto.getTrainerId(), dto.getTrainerUsername(), 2);

        List<UserReceiveNotification> usersReceivedotificaction=new ArrayList<>();
        Notification notification=notificationRepository.save(new Notification("Assigned for a session", "you are assigned as the the instructor for a training session at "+dto.getDate()+" from "+dto.getStartTime()+" to "+dto.getEndTime(), LocalDate.now(),1,usersReceivedotificaction,null));
        userReceiveNotificationRepository.save(new UserReceiveNotification(1, LocalDateTime.now(),TrainerUser,notification));

    }

    private Branch getUserBranch(int id,String username){
        System.out.println(username);
        Student student=null;
        LinkedList<Student> stuList;
        if(id==0){
            stuList=studentRepository.findByUsername(username);
            for (Student student1 : stuList) {
                student = student1;
            }
        }else{
            student=studentRepository.findById(id).get();
        }
        return student.getBranch();
    }

    public LinkedList<SessionDTO> getAllSessions(StudentDTO dto) {
        Branch branch = getUserBranch(dto.getStuID(), dto.getUsername());
        LinkedList<Session> sessions = sessionRepository.findAllByBranch(branch);
        LinkedList<SessionDTO> retList = new LinkedList<>();
        for (Session session : sessions) {
            if (session.getStatus() == 1) {
                retList.add(new SessionDTO(session.getSessionId(), session.getTrainer().getFullName(), session.getDate(), session.getStatus(), session.getNumOfStudent(), session.getRoute(), session.getStartTime(), session.getEndTime(),session.getType().getTypeName()));//code to complete
            }
        }

        if (retList.isEmpty()) {
            return retList;
        } else{
            return retList;
        }
    }

    public int getAvailSeats(int sessionId){
        Session session=sessionRepository.findById(sessionId).get();
        int count=0;
        for (StuSession stuSession : session.getStuSessions()) {
            count++;
        }

        return count;
    }

    private Student giveStudent(String username){
        LinkedList<Student> list=studentRepository.findByUsername(username);
        Student student=null;
        for (Student student1 : list) {
            student = student1;
        }
        return student;
    }

    private Cource giveCource(Student student){
        List<Cource> cources=student.getCourceList();
        Cource cource=null;
        for (Cource cource1 : cources) {
            if(cource1.getStatus()==0){
                cource=cource1;
            }
        }
        return cource;
    }

    private boolean isBookedBefore(Student student,Session session){
        StuSession stuSession=null;
        boolean val=false;
        for (StuSession sessionStuSession : session.getStuSessions()) {
            if(sessionStuSession.getStudent()==student){
                val=true;
            }
        }
        return val;
    }

    @Transactional
    public boolean bookSession(SessionDTO dto){
        Session session=sessionRepository.findById(dto.getSessionId()).get();
        Student student=giveStudent(dto.getStudentUsername());
        Cource cource=giveCource(student);
        if(isBookedBefore(student,session)==true){
            return false;
        }else{
            stuSessionRepository.save(new StuSession(session.getDate(),1,session,cource,student));
            return true;
        }
    }

    public LinkedList<SessionDTO> viewAllBooking(SessionDTO dto){
        Student student=giveStudent(dto.getStudentUsername());
        LinkedList<SessionDTO> sessionDTOS=new LinkedList<>();
        for (StuSession allByStudentAndStatus : stuSessionRepository.findAllByStudentAndStatus(student, 1)) {
            sessionDTOS.add(new SessionDTO(allByStudentAndStatus.getSession().getSessionId(), allByStudentAndStatus.getSession().getTrainer().getFullName(), allByStudentAndStatus.getSession().getDate(), allByStudentAndStatus.getSession().getStatus(), allByStudentAndStatus.getSession().getNumOfStudent(), allByStudentAndStatus.getSession().getRoute(), allByStudentAndStatus.getSession().getStartTime(), allByStudentAndStatus.getSession().getEndTime(),allByStudentAndStatus.getSession().getType().getTypeName()));
        }
        if(sessionDTOS.isEmpty()){
            return sessionDTOS;
        }else{
            return sessionDTOS;
        }

    }
}
