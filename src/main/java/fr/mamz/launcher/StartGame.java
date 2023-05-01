package fr.mamz.launcher;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class StartGame extends GameApplication {
    //https://webtechie.be/post/2020-05-07-getting-started-with-fxgl/

    private final int WIDTH = 800, HEIGHT = 600;
    private GeneratorMob generatorMob;
    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("BTS GAME");
        settings.setVersion("1.0");
        this.generatorMob = new GeneratorMob();
    }

    @Override
    protected void initPhysics() {

        /*
        Nous avons mis en place deux gestionnaires de collision. Le premier gère la collision entre le type de balle et le type d'ennemi.
        Lorsqu'une telle collision se produit, nous supprimons simplement les deux entités du jeu.
        Dans le deuxième gestionnaire, nous affichons une boîte de dialogue d'information avec le texte « Tu es mort ! » et redémarrez le jeu.
         */

        onCollisionBegin(EntityType.PROJECTILE, EntityType.ENEMY, (projectile, enemy) -> {
            projectile.removeFromWorld();
            enemy.removeFromWorld();
            inc("kills", + 1);
        });

        onCollisionBegin(EntityType.ENEMY, EntityType.PLAYER, (enemy, player) -> {
            showMessage("Vous êtes mort ! Merci d'avoir jouer !", () -> {
                getGameController().startNewGame();
            });
        });
    }

    @Override
    protected void initGame() {

        /*
        Tout d'abord, nous ajoutons notre usine au monde du jeu, afin de pouvoir utiliser des méthodes telles que spawn().
        Ensuite, nous initialisons notre player référence en faisant apparaître l'entité joueur au centre du jeu.
        Nous devons également fournir ce lecteur à notre geoWarsFactory car elle en a besoin pour définir le point de départ des nouvelles balles.

        Le dernier appel configure une minuterie qui s'exécute toutes les secondes.
        Pouvez-vous deviner ce qui se passe à chaque seconde ? La réponse est : spawn("enemy"), c'est-à-dire qu'une nouvelle entité ennemie est générée.
        Comme nous ne fournissons aucune position, les entités ennemies apparaîtront à (0,0).
         */

        getGameWorld().addEntityFactory(this.generatorMob);

        this.player = spawn("Player", getAppWidth() / 2, getAppHeight() / 2);

//        *##
//        ###
//        ###
        run(() -> spawn("Enemy", 0, 0), Duration.seconds(1.0));

//        #*#
//        ###
//        ###
        run(() -> spawn("Enemy", WIDTH / 2, 0), Duration.seconds(1.0));

//        ##*
//        ###
//        ###
        run(() -> spawn("Enemy", WIDTH, 0), Duration.seconds(1.0));

//        ###
//        ##*
//        ###
        run(() -> spawn("Enemy", WIDTH, HEIGHT / 2), Duration.seconds(1.0));

//        ###
//        ###
//        ##*
        run(() -> spawn("Enemy", WIDTH, HEIGHT), Duration.seconds(1.0));

//        ###
//        ###
//        #*#
        run(() -> spawn("Enemy", WIDTH / 2, HEIGHT), Duration.seconds(1.0));

//        ###
//        ###
//        *##
        run(() -> spawn("Enemy", 0, HEIGHT), Duration.seconds(1.0));

//        ###
//        *##
//        ###
        run(() -> spawn("Enemy", 0, HEIGHT / 2), Duration.seconds(1.0));

    }

    @Override
    protected void initInput() {

        /*
        Le code ci-dessus doit être explicite et utilise la notation Java 8+ lambda.
        Si vous souhaitez pouvoir tirer pendant que le bouton de la souris est enfoncé, plutôt que sur une seule pression, vous pouvez passer onBtnDown à onBtn.
         */

        onKey(KeyCode.Z, () -> player.translateY(-2));
        onKey(KeyCode.Q, () -> player.translateX(-2));
        onKey(KeyCode.S, () -> player.translateY(2));
        onKey(KeyCode.D, () -> player.translateX(2));
        onBtnDown(MouseButton.PRIMARY, () -> spawn("Projectile", this.player.getCenter()));
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("kills", 0);
    }

    @Override
    protected void initUI() {
        Text kills = new Text();
        kills.setTranslateX(WIDTH / 2);
        kills.setTranslateY(10);

        kills.textProperty().bind(getWorldProperties().intProperty("kills").asString());

        getGameScene().addUINode(kills);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

