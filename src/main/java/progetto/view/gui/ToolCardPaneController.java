package progetto.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import progetto.controller.UseToolCardAction;
import progetto.model.IModel;
import progetto.model.PreGameState;
import progetto.model.ToolCard;

import java.util.List;

/**
 * this is the class that handles the tool card fxml. This class is only instanced by javafx, this mean that
 * must have a default constructor.
 * @author Federica
 */
public class ToolCardPaneController extends AbstractController{

    @FXML
    private TilePane toolCardTilePane;
    @FXML
    private VBox extraToolCardVBox;
    @FXML
    private HBox mainBox;
    private int displayedToolCards = -1;
    @FXML
    private ChangeDiceValuePaneController changeDiceValuePaneController;
    private static final int STANDARD_NUMBER_OF_TOOL_CARDS = 3;
    private static final int BACK_TOOL_CARD = 13;

    /**
     * set up this object, it is equivalent to a constructor since there is no access to it
     * @param view the current gui view
     */
    @Override
    public void setUp(GUIView view){
    	super.setUp(view);
        changeDiceValuePaneController.setUp(view);
        for (int i = 0; i< STANDARD_NUMBER_OF_TOOL_CARDS; i++){
            ImageView imageView = (ImageView) toolCardTilePane.getChildren().get(i);
            final int j = i;
            imageView.setOnMouseClicked(event -> onMouseClicked(j));
        }
        for (int i = 0; i<2; i++){
            ImageView imageView = (ImageView) extraToolCardVBox.getChildren().get(i);
            final  int j = i + 3;
            imageView.setOnMouseClicked(event -> onMouseClicked(j));
        }
        view.getController().getObservable().getMainBoard().addObserver(ogg -> Platform.runLater(this::update));
    }

    /**
     * called when the imageView of a card is clicked
     * @param i position of the card in the main board
     */
    private void onMouseClicked(int i){
        UseToolCardAction useToolCardAction = new UseToolCardAction(getController().getChair(), i);
        if (useToolCardAction.canBeExecuted(getController().getModel()))
            getController().sendAction(useToolCardAction);
    }

    /**
     * called when main board changes
     * update displayed cards
     */
    private void update(){
        IModel model = getController().getModel();
        List<ToolCard> toolCardList = model.getMainBoard().getData().getToolCards();
        TextureDatabase textureDatabase = TextureDatabase.getTextureDatabase();

        updateToolCards(model, toolCardList, textureDatabase);
        displayedToolCards = model.getMainBoard().getData().getToolCards().size();
    }

    /**
     * update single or multi player tool cards
     * @param model current model
     * @param toolCardList list of current tool cards
     * @param textureDatabase texture database to use
     */
    private void updateToolCards(IModel model, List<ToolCard> toolCardList, TextureDatabase textureDatabase){
        if (model.getMainBoard().getData().getPlayerCount()!=1)
            updateToolCardsMultiPlayer(toolCardList, textureDatabase);
        else updateToolCardSinglePlayer(model, toolCardList, textureDatabase);
    }

    /**
     * update multi player tool cards
     * @param toolCardList list of current tool cards
     * @param textureDatabase texture database to use
     */
    private void updateToolCardsMultiPlayer(List<ToolCard> toolCardList, TextureDatabase textureDatabase){
        if (mainBox.getChildren().contains(extraToolCardVBox))
            mainBox.getChildren().removeAll(extraToolCardVBox);
        loadStandardToolCards(STANDARD_NUMBER_OF_TOOL_CARDS, toolCardList, textureDatabase);
    }

    /**
     * update single player tool cards
     * @param model current model
     * @param toolCardList list of current tool cards
     * @param textureDatabase texture database to use
     */
    private void updateToolCardSinglePlayer(IModel model, List<ToolCard> toolCardList, TextureDatabase textureDatabase){
        int currentToolCards;
        if (model.getMainBoard().getData().getGameState().getClass() == PreGameState.class)
            currentToolCards = model.getMainBoard().getData().getDifficulty();
        else currentToolCards = model.getMainBoard().getData().getToolCards().size();
        if (currentToolCards!=displayedToolCards){
            if (currentToolCards <= STANDARD_NUMBER_OF_TOOL_CARDS){
                if (mainBox.getChildren().contains(extraToolCardVBox))
                {
                    mainBox.getChildren().clear();
                    Region firstRegion = new Region();
                    Region lastRegion = new Region();
                    HBox.setHgrow(firstRegion, Priority.ALWAYS);
                    HBox.setHgrow(lastRegion, Priority.ALWAYS);
                    mainBox.getChildren().add(firstRegion);
                    mainBox.getChildren().add(toolCardTilePane);
                    mainBox.getChildren().add(lastRegion);
                }
            }
            else if (!mainBox.getChildren().contains(extraToolCardVBox))
                mainBox.getChildren().add(extraToolCardVBox);
        }
        if (currentToolCards>STANDARD_NUMBER_OF_TOOL_CARDS){
            loadStandardToolCards(STANDARD_NUMBER_OF_TOOL_CARDS, toolCardList, textureDatabase);
            loadExtraToolCards(currentToolCards, toolCardList, textureDatabase);
        }
        else loadStandardToolCards(currentToolCards, toolCardList, textureDatabase);
        displayedToolCards = currentToolCards;
    }

    /**
     * update the first 3 tool cards in the main board
     * @param currentToolCards current number of tool cards
     * @param toolCardList list of current tool cards
     * @param textureDatabase texture database to use
     */
    private void loadStandardToolCards(int currentToolCards, List<ToolCard> toolCardList,
                                       TextureDatabase textureDatabase){
        ImageView imageView;
        if (displayedToolCards!=currentToolCards && currentToolCards<STANDARD_NUMBER_OF_TOOL_CARDS)
            for (int j = 0; j<STANDARD_NUMBER_OF_TOOL_CARDS; j++){
                imageView = (ImageView) toolCardTilePane.getChildren().get(j);
                imageView.setImage(null);
            }
        for (int i = 0; i< currentToolCards; i++){
            imageView = (ImageView) toolCardTilePane.getChildren().get(i);
            if (toolCardList.isEmpty()){
                if (imageView.getImage()!=textureDatabase.getToolCard(BACK_TOOL_CARD))
                    imageView.setImage(textureDatabase.getToolCard(BACK_TOOL_CARD));}
            else {
                if (imageView.getImage() != textureDatabase.getToolCard(toolCardList.get(i).getIndex()))
                    imageView.setImage(textureDatabase.getToolCard(toolCardList.get(i).getIndex()));
            }
        }
    }

    /**
     * update the extra tool cards in the main board ( used only in single player mode )
     * @param currentToolCards current number of tool cards
     * @param toolCardList list of current tool cards
     * @param textureDatabase texture database to use
     */
    private void loadExtraToolCards(int currentToolCards, List<ToolCard> toolCardList,
                                    TextureDatabase textureDatabase){
        ImageView imageView;
        int realNumberOfToolCard = STANDARD_NUMBER_OF_TOOL_CARDS;
        if (currentToolCards!=displayedToolCards)
            for (int j = 0; j<2; j++){
                imageView = (ImageView) extraToolCardVBox.getChildren().get(j);
                imageView.setImage(null);
            }
        int numberOfExtraToolCards = currentToolCards - STANDARD_NUMBER_OF_TOOL_CARDS;
        for (int i = 0; i<numberOfExtraToolCards; i++){
            imageView = (ImageView) extraToolCardVBox.getChildren().get(i);
            if (toolCardList.isEmpty()){
                if (imageView.getImage()!=textureDatabase.getToolCard(BACK_TOOL_CARD)){
                    imageView.setImage(textureDatabase.getToolCard(BACK_TOOL_CARD));
                }
            }
            else {
                if (imageView.getImage()!=textureDatabase.getToolCard(toolCardList.get(realNumberOfToolCard).getIndex()))
                    imageView.setImage(textureDatabase.getToolCard(toolCardList.get(realNumberOfToolCard).getIndex()));
            }
            realNumberOfToolCard++;
        }
    }
}
