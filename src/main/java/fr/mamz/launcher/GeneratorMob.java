package fr.mamz.launcher;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.dsl.components.RandomMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class GeneratorMob implements EntityFactory {

    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data) {

        /*
        Premièrement, la signature de la méthode est importante. Il a une annotation que nous pouvons utiliser plus tard pour faire apparaître un joueur. Ensuite, regardons comment nous définissons le joueur.
        Nous utilisons entityBuilder()pour nous aider à le faire.
        .from(data) définit des propriétés typiques telles que la position, qui est obtenue à partir de SpawnData data.
        Nous définissons également le type de l'entité via .type(EntityType.PLAYER).
        La ligne suivante .viewWithBBox(new Rectangle(30, 30, Color.BLUE)) a deux objectifs : a) elle fournit la vue rectangulaire pour le joueur et b) elle génère une boîte englobante pour les collisions à partir de la vue.
        Enfin, nous marquons l'entité comme .collidable() et la construisons.
         */

        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .viewWithBBox(new Rectangle(40, 40, Color.GREENYELLOW))
                .collidable()
                .build();
    }

    @Spawns("Projectile")
    public Entity spawnProjectile(SpawnData data) {

        /*
        Nous obtenons d'abord l'instance du joueur.
        La deuxième ligne calcule la direction dans laquelle la balle se déplacera lors de sa création, en partant du centre des joueurs.
        En excluant l'API que nous avons déjà couverte ci-dessus, .with(new ProjectileComponent(direction, 1000)) et .with(new OffscreenCleanComponent()) attachez des composants à notre entité bullet.
        Un composant peut contenir des données et un comportement et apporter de nouvelles fonctionnalités à une entité.
        Par exemple, ProjectileComponent déplace l'entité à chaque image direction avec la vitesse donnée.
        OffscreenCleanComponent, comme son nom l'indique, supprime l'entité du jeu si elle se trouve au-delà des limites de l'écran.
         */

        Entity player = getGameWorld().getSingleton(EntityType.PLAYER);
        Point2D directionFromProjectile = getInput().getMousePositionWorld().subtract(player.getCenter());

        return entityBuilder(data)
                .type(EntityType.PROJECTILE)
                .viewWithBBox(new Rectangle(10, 2, Color.DARKRED))
                .collidable()
                .with(new ProjectileComponent(directionFromProjectile, 1000))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data) {

        /*
        Pour notre ennemi, nous utiliserons un cercle comme vue.
        La plupart des méthodes ont déjà été couvertes ci-dessus, nous allons donc nous concentrer sur RandomMoveComponent.
        Ce composant, en utilisant les limites rectangulaires fournies, déplace l'entité de manière aléatoire dans ces limites.
        Ce comportement simple est suffisant pour notre petit jeu.
        Nous en avons fini avec la classe d'usine maintenant !
         */

        Circle circle = new Circle(20, 20, 20, Color.BLANCHEDALMOND);
        circle.setStroke(Color.BROWN);
        circle.setStrokeWidth(2.0);

        return entityBuilder(data)
                .type(EntityType.ENEMY)
                .viewWithBBox(circle)
                .collidable()
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth(), getAppHeight()), 50))
                .build();
    }

}
