package braun.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Ui} persona remark matching logic.
 */
public class UiTest {

    @Test
    public void getRemarkForTask_loreKeywordGhost_anomalyRemarkReturned() {
        Ui ui = new Ui();
        String remark = ui.getRemarkForTask("explore ghost story");
        assertTrue(remark.contains("A new anomaly! Daydream Inc. will want this documented."));
    }

    @Test
    public void getRemarkForTask_loreKeywordWork_workRemarkReturned() {
        Ui ui = new Ui();
        String remark = ui.getRemarkForTask("finish monthly work report");
        assertTrue(remark.contains("Even trapped in a ghost story, we still gotta work, don't we?"));
    }

    @Test
    public void getRemarkForTask_loreKeywordRabbit_rabbitRemarkReturned() {
        Ui ui = new Ui();
        String remark = ui.getRemarkForTask("find pink rabbit doll");
        assertTrue(remark.contains("Reminds me of a certain charming pink rabbit doll, doesn't it?"));
    }

    @Test
    public void getRemarkForTask_unrecognizedDescription_generalRemarkReturned() {
        Ui ui = new Ui();
        String remark = ui.getRemarkForTask("arbitrary generic task 12345");
        assertNotNull(remark);
        assertFalse(remark.isEmpty());
    }
}
