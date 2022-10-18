package fr.mamz.launcher;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onBtnDown;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.showMessage;
import static com.almasb.fxgl.dsl.FXGL.spawn;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
public class HelloApplication extends GameApplication {

    //https://webtechie.be/post/2020-05-07-getting-started-with-fxgl/

    private GeneratorMob generatorMob;
    private Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("First game");
        this.generatorMob = new GeneratorMob();
//        getGameWorld().getProperties().
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
        Ensuite, nous initialisons notre playerréférence en faisant apparaître l'entité joueur au centre du jeu.
        Nous devons également fournir ce lecteur à notre geoWarsFactory car elle en a besoin pour définir le point de départ des nouvelles balles.

        Le dernier appel configure une minuterie qui s'exécute toutes les secondes.
        Pouvez-vous deviner ce qui se passe à chaque seconde ? La réponse est : spawn("enemy"), c'est-à-dire qu'une nouvelle entité ennemie est générée.
        Comme nous ne fournissons aucune position, les entités ennemies apparaîtront à (0,0).
         */

        getGameWorld().addEntityFactory(this.generatorMob);

        this.player = spawn("Player", getAppWidth() / 2, getAppHeight() / 2);
        run(() -> spawn("Enemy"), Duration.seconds(1.0));
        run(() -> spawn("Enemy", 800, 600), Duration.seconds(1.0));
        run(() -> spawn("Enemy", 0, 600), Duration.seconds(1.0));
        run(() -> spawn("Enemy", 800, 0), Duration.seconds(1.0));

    }

    @Override
    protected void initInput() {

        /*
        Le code ci-dessus doit être explicite et utilise la notation Java 8+ lambda.
        Si vous souhaitez pouvoir tirer pendant que le bouton de la souris est enfoncé, plutôt que sur une seule pression, vous pouvez passer onBtnDown à onBtn.
         */

        onKey(KeyCode.Z, () -> player.translateY(-5));
        onKey(KeyCode.Q, () -> player.translateX(-5));
        onKey(KeyCode.S, () -> player.translateY(5));
        onKey(KeyCode.D, () -> player.translateX(5));
        onBtnDown(MouseButton.PRIMARY, () -> spawn("Projectile", this.player.getCenter()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}