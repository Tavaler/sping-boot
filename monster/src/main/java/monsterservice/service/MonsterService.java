package monsterservice.service;

import monsterservice.handleExceptionError.HandleExceptionError;
import monsterservice.model.Monster;
import monsterservice.repostory.MonsterRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonsterService {

    @Autowired
    private MonsterRepostory monsterRepostory;


    public Monster postCreateMonsterService(
            Monster monster) {

        return monsterRepostory.save(monster);
    }

    public List<Monster> getAllMonsterService() {
        return monsterRepostory.findAll();
    }

    public Optional<Monster> getInformationService(Integer id) {
        // Monster monster = new Monster();
        // monster.setId(id);
        return monsterRepostory.findById(id);
    }


    public Monster updateMonsterByIdService(
            Monster monster)
            throws HandleExceptionError {
        try {
            Optional<Monster> data = getInformationService(monster.getId());
            if (data.isPresent()) {
                return monsterRepostory.save(monster);
            } else {
                throw new HandleExceptionError(
                        "Data not found");
            }
        } catch (RuntimeException ex) {
            throw new HandleExceptionError("Can't connect database");
        } catch (Exception ex) {
            throw new HandleExceptionError(
                    ex.getMessage());
        }
    }


    public boolean deleteMonsterService(Integer id) throws HandleExceptionError {

        try {
            Optional<Monster> data = getInformationService(id);
            if (data.isPresent()) {
                monsterRepostory.deleteById(id);
                return true;
            } else throw new HandleExceptionError(
                    "Data not found");
        } catch (HandleExceptionError ex) {
            throw new HandleExceptionError(
                    "can't connect database");
        }
    }


    public String attackMonsterService
            (Integer id, Integer damage)
            throws HandleExceptionError {
        try {
            Optional<Monster> data = getInformationService(id);
            if (data.isPresent()) {
                int healthNow = data.get().getHealth() - damage;
                if (healthNow < 0) healthNow = 0;
                int response = monsterRepostory
                        .attackMonster(id, healthNow);
                if (response != 0) {
                    return "Update Success";
                } else return "can't update";

            } else throw new HandleExceptionError("data not found");

        } catch (HandleExceptionError ex) {
            throw new HandleExceptionError(ex.getMessage());
        }


    }


}
