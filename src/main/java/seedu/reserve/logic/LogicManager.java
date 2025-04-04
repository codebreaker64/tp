package seedu.reserve.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.reserve.commons.core.GuiSettings;
import seedu.reserve.commons.core.LogsCenter;
import seedu.reserve.logic.commands.Command;
import seedu.reserve.logic.commands.CommandResult;
import seedu.reserve.logic.commands.exceptions.CommandException;
import seedu.reserve.logic.parser.ReserveMateParser;
import seedu.reserve.logic.parser.exceptions.ParseException;
import seedu.reserve.model.Model;
import seedu.reserve.model.ReadOnlyReserveMate;
import seedu.reserve.model.reservation.Reservation;
import seedu.reserve.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final ReserveMateParser reserveMateParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        reserveMateParser = new ReserveMateParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = reserveMateParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveReserveMate(model.getReserveMate());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyReserveMate getReserveMate() {
        return model.getReserveMate();
    }

    @Override
    public ObservableList<Reservation> getFilteredReservationList() {
        return model.getFilteredReservationList();
    }

    @Override
    public HashMap<String, Integer> getReservationStatistics() {
        return model.getReservationStatistics();
    }

    @Override
    public Path getReserveMateFilePath() {
        return model.getReserveMateFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
