package com.sp.tools;

import com.sp.model.Card;
import com.sp.model.CardTemplate;
import com.sp.model.EAffinity;
import com.sp.model.EFamily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardFactory {

    // HashMap of card templates with probabilities of loot
    private static HashMap<CardTemplate, Float> cardTemplates = new HashMap<>();

    static {
        init();
    }

    public static void init() {
        CardTemplate cardTemplate1 = CardTemplate
                .builder()
                .name("AquaGirl")
                .description("Née sous les vagues, elle est capable de contrôler l'eau à sa guise. D'une agilité remarquable, elle peut nager à une vitesse phénoménale et communiquer avec les créatures marines. Son pouvoir s'étend jusqu'à la capacité de manipuler la température de l'eau pour créer des formations glacées ou des jets de vapeur brûlants. Elle utilise ses pouvoirs pour causer des inondations et pour semer le chaos dans les villes côtières.")
                .family(EFamily.SUPER_VILLAINS.getName())
                .affinity(EAffinity.WATER.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/aquagirl.jpg")
                .build();
        CardTemplate cardTemplate2 = CardTemplate
                .builder()
                .name("Joke")
                .description(" Né dans une famille de clowns, il a développé un sens de l'humour tordu et malsain. Il aime semer la joie et la bonne humeur dans la ville en organisant des événements loufoques et distrayants. Son passé est empreint de souffrance et de solitude, mais il a choisi de transformer sa douleur en une mission pour faire rire les autres. Son humour tordu n'est souvent pas apprécié par les autres, mais il ne se laisse pas décourager et continue à faire ce qu'il aime.")
                .family(EFamily.SUPER_HEROES.getName())
                .affinity(EAffinity.FIRE.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/joke.jpg")
                .build();
        CardTemplate cardTemplate3 = CardTemplate
                .builder()
                .name("Catman")
                .description("Depuis sa plus tendre enfance, il a été fasciné par les chats. Il a développé une agilité remarquable, une vision nocturne et une capacité à communiquer avec les félins. Il a utilisé ces compétences pour commettre des crimes et pour infiltrer les maisons de riches. Son costume inspiré de cet animal démontre son désir de voler les richesses des autres. Il est souvent accompagné de ses fidèles chats qui l'aident dans ses méfaits.")
                .family(EFamily.SUPER_VILLAINS.getName())
                .affinity(EAffinity.EARTH.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/catman.jpg")
                .build();
        CardTemplate cardTemplate4 = CardTemplate
                .builder()
                .name("SuperAlien")
                .description("Originaire d'une planète lointaine, il possède des pouvoirs extraordinaires, comme la super-force, la super-vitesse et la super-endurance. Il a été envoyé sur Terre pour conquérir la planète et dominer les êtres humains. Depuis son arrivée, il a utilisé ses pouvoirs pour semer le chaos et la destruction.")
                .family(EFamily.SUPER_VILLAINS.getName())
                .affinity(EAffinity.SPACE.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/superalien.jpg")
                .build();
        CardTemplate cardTemplate5 = CardTemplate
                .builder()
                .name("Wonder Man")
                .description("Né dans une famille de scientifiques, il a été exposé à une énergie mystérieuse qui lui a donné une force surhumaine et une résistance incroyable. Mais son égoïsme et son désir de pouvoir l'ont poussé à utiliser ses pouvoirs pour commettre des crimes et pour semer la terreur dans la ville. Il a été arrêté par les autorités et emprisonné dans une prison de haute sécurité. Mais il a réussi à s'échapper et à reprendre ses activités criminelles.")
                .family(EFamily.SUPER_VILLAINS.getName())
                .affinity(EAffinity.TIME.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/wonderman.jpg")
                .build();
        CardTemplate cardTemplate6 = CardTemplate
                .builder()
                .name("Penguin")
                .description("Issu d'une famille de riches marchands, il a acquis une grande intelligence et une grande ruse. Criminel dans un premier temps, mais suite à sa conversion au toupisme, il a mis ses compétences au service de la justice en devenant un détective privé renommé. Son passé est marqué par une soif de pouvoir et de richesse, il a choisi de mettre ses compétences au service des autres en luttant contre le crime.")
                .family(EFamily.SUPER_HEROES.getName())
                .affinity(EAffinity.WATER.getName())
                .hp(RandomUtils.randomIntCallback(0, 100))
                .attack(RandomUtils.randomIntCallback(0, 100))
                .defense(RandomUtils.randomIntCallback(0, 100))
                .energy(RandomUtils.randomIntCallback(0, 100))
                .imageUrl("images/penguin.jpg")
                .build();
        float amount = (float) (1.0/6.0);
        cardTemplates.put(cardTemplate1, amount);
        cardTemplates.put(cardTemplate2, amount);
        cardTemplates.put(cardTemplate3, amount);
        cardTemplates.put(cardTemplate4, amount);
        cardTemplates.put(cardTemplate5, amount);
        cardTemplates.put(cardTemplate6, amount);
    }

    public static List<Card> generateRandomListFromTemplates(int size){
        List<Card> cards = new ArrayList<>();
        System.out.println("size : " + size);
        for(int i = 0; i < size; i++){
            cards.add(generateRandomFromTemplates());
        }
        return cards;
    }

    public static Card generateRandomFromTemplates(){
        CardTemplate cardTemplate = RandomUtils.randomFromMap(cardTemplates);
        assert cardTemplate != null;
        return generateFromTemplate(cardTemplate);
    }

    private static Card generateFromTemplate(CardTemplate cardTemplate) {
        return cardTemplate.getRandomCard();
    }

}
