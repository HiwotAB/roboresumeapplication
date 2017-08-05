package hiwotab.roboresumeapplication.Controller;

import hiwotab.roboresumeapplication.Models.Resume;
import hiwotab.roboresumeapplication.repositories.ResumeRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class MainController {
    DateFormat df=new SimpleDateFormat("MM/dd/YYYY");
    Date date =new Date();

    @Autowired
    ResumeRepostory resumeRepostory;

    @GetMapping("/homePage")
    public String showHomePage(Model model) {
        String myMessage = "Welcome to Robo Resume Application";
        model.addAttribute("message", myMessage);
        return "homePage";
    }

    @GetMapping("/addUser")
    public String addUserInfo(Model model) {
        model.addAttribute("newUser", new Resume());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String addUserInfo(@Valid @ModelAttribute("newUser") Resume resume, BindingResult bindingResult)throws ParseException {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }

       /* if(resume.getEndDate()!=null){
            resume.setEndDate(df.format(date));
        }
        else
            resume.setEndDate(df.getEndDate());
            */
        String dateS=resume.getStartDate();
        String dateE=resume.getEndDate();
        Date dateStart=df.parse(dateS);
        Date dateEnd=df.parse(dateE);
        long difference=dateStart.getTime()-dateEnd.getTime();
        long numberOfDays=difference/86400000;
        long diff=Math.abs(numberOfDays);
        resume.setWorkExp(diff);
        resumeRepostory.save(resume);
        return "dispUserInfo";
    }

    @GetMapping("/listAllUsers")
    public String listAllUser(Model model) {
        Iterable<Resume> resumeList = resumeRepostory.findAll();
        model.addAttribute("searchUser", resumeList);
        return "listUser";

    }

    @GetMapping("/workExp")
    public String calcDateDiff(Model model) throws ParseException {
        Iterable<Resume> resumeList = resumeRepostory.findAll();
        model.addAttribute("searchUser", resumeList);
        return "dipWorkExp";
    }



    /*public Resume findEmployeeBySsn(String ssn) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("ssn", ssn));
        return (Employee) criteria.uniqueResult();
    }


     /*This method will provide the medium to update an existing employee.

    @RequestMapping(value = { "/edit-{ssn}-employee" }, method = RequestMethod.GET)
    public String editEmployee(@PathVariable String ssn, ModelMap model) {
        Employee employee = service.findEmployeeBySsn(ssn);
        model.addAttribute("employee", employee);
        model.addAttribute("edit", true);
        return "registration";
    }

    /*
     * This method will be called on form submission, handling POST request for
     * updating employee in database. It also validates the user input

    @RequestMapping(value = { "/edit-{ssn}-employee" }, method = RequestMethod.POST)
    public String updateEmployee(@Valid Employee employee, BindingResult result,
                                 ModelMap model, @PathVariable String ssn) {

        if (result.hasErrors()) {
            return "registration";
        }

        if(!service.isEmployeeSsnUnique(employee.getId(), employee.getSsn())){
            FieldError ssnError =new FieldError("employee","ssn",messageSource.getMessage("non.unique.ssn", new String[]{employee.getSsn()}, Locale.getDefault()));
            result.addError(ssnError);
            return "registration";
        }

        service.updateEmployee(employee);

        model.addAttribute("success", "Employee " + employee.getName()  + " updated successfully");
        return "success";
    }


    * */

}