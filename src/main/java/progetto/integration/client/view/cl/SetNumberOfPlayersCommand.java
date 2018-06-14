package progetto.integration.client.view.cl;

import progetto.controller.SetPlayerCountAction;

public class SetNumberOfPlayersCommand extends AbstractStateSwitcherCommand {

    private static final int MAX_NUMBER_OF_PLAYERS = 4;

    public SetNumberOfPlayersCommand(CommandLineView commandLineView) {
        super(commandLineView, new GameTransitionState(commandLineView));
    }

    @Override
    protected void perform(String[] params) {

        int numberOfPlayers;

        try {
            numberOfPlayers = Integer.parseInt(params[0]);
        }catch (Exception e){
            numberOfPlayers = -1;
        }

        if(numberOfPlayers < 1 || numberOfPlayers > MAX_NUMBER_OF_PLAYERS){
            write("Inserire un numero di giocatori valido!\n");
            return;
        }

        getController().sendAction(new SetPlayerCountAction((numberOfPlayers)));

    }

    @Override
    public String getName() {
        return "2";
    }

    @Override
    public String getHelp() {
        return "Camabia il numero di giocatori (Formato: 2 <Numero di giocatori desiderato>)";
    }
}
