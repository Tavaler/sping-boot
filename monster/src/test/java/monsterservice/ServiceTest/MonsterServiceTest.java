package monsterservice.ServiceTest;

import monsterservice.handleExceptionError.HandleExceptionError;
import monsterservice.model.Monster;
import monsterservice.repostory.MonsterRepostory;
import monsterservice.service.MonsterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class MonsterServiceTest {
    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepostory monsterRepostory;

    private Monster mockMonster() {
        Monster mockMonster = new Monster();
        mockMonster.setId(1);
        mockMonster.setName("name");
        mockMonster.setHealth(400);
        return mockMonster;

    }

    @Test
    void postCreateMonsterTest() {
        doReturn(mockMonster())
                .when(monsterRepostory)
                .save(any(Monster.class));

        Monster response = monsterService
                .postCreateMonsterService(new Monster());

        assertEquals(mockMonster().getId(), response.getId());
        assertEquals(mockMonster().getName(), response.getName());
        assertEquals(mockMonster().getHealth(), response.getHealth());
    }


    @Test
    void getAllMonsterTest() {
        List<Monster> monsterList = new ArrayList<>();
        monsterList.add(mockMonster());

        doReturn(monsterList)
                .when(monsterRepostory).findAll();

        List<Monster> response = monsterService.getAllMonsterService();
        assertEquals(monsterList, response);
    }


//    @Test
//    void getInformationTest() {
//        List<Monster> monsterList = new ArrayList<>();
//        monsterList.add(mockMonster());
//
//        doReturn(monsterList)
//                .when(monsterRepostory).findAllById(Integer.class);
//
//        List<Monster> response = monsterService.getAll();
//        assertEquals(monsterList, response);
//    }

    @Test
    void putUpdateMonsterServiceTist() throws HandleExceptionError {

        doReturn(Optional.of(mockMonster()))
                .when(monsterRepostory)
                .findAllById(any());

        doReturn(mockMonster())
                .when(monsterRepostory)
                .save(any(Monster.class));

        Monster reqestMonster = new Monster();
        reqestMonster.setId(1);

        Monster response = monsterService
                .updateMonsterByIdService(reqestMonster);

        assertEquals(mockMonster().getId(), response.getId());
    }


    @Test
    void putUpdateMonsterServiceNotFound() {
        HandleExceptionError handleExceptionError
                = assertThrows(HandleExceptionError.class,
                () -> monsterService.updateMonsterByIdService(new Monster())
        );

        assertEquals("Data not found", handleExceptionError.getMessage());
    }


//    @Test
//    void putUpdateMonsterServiceFailCanNotDatabase() {
//        doThrow(new RuntimeException())
//                .when(monsterRepostory)
//                .findById(any());
//
//        HandleExceptionError handleExceptionError
//                = assertThrows(HandleExceptionError.class,
//                () -> monsterService.updateMonsterByIdService(new Monster()));
//
//        assertEquals("Data not found",
//                handleExceptionError.getMessage());
//    }

    @Test
    void UpdateMonsterServiceFailCanNotConnectDatabase() {
        doThrow(new RuntimeException())
                .when(monsterRepostory)
                .findById(any());
        //ConnectException
        HandleExceptionError handleExceptionError
                = assertThrows(HandleExceptionError.class, () ->
                monsterService.updateMonsterByIdService(new Monster()));

        assertEquals("Can't connect database",
                handleExceptionError.getMessage());
    }


}
