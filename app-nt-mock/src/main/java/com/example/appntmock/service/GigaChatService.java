package com.example.appntmock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GigaChatService {

    private final ChatClient chatClient;

    public Flux<String> stream() {
        return chatClient.prompt(prompt + text)
                .stream()
                .chatResponse()
                .log()
                .mapNotNull(rs -> rs.getResult().getOutput().getText());
    }

    private static final String prompt = """
            Напиши мне следующий текст. Напиши только его и ничего больше. Текст должен быть полностью идентичен.
            
            """;

    private static final String text = """
            Утро в этом городе начиналось всегда одинаково — неспешно, с лёгкой прохладой, проникающей сквозь щели оконных рам, и шумом первых трамваев, с гулом разливающимся по улицам. Казалось, сама архитектура города дышит вместе с его жителями: старые дома с облупленной штукатуркой, чугунные балконы, гудящие водостоки — всё это создаёт впечатление, будто время здесь течёт иначе. Люди, привыкшие к этому ритму, не спешат. Они пьют утренний кофе не на бегу, а сидя у окна, глядя, как первые лучи солнца касаются карнизов, как коты лениво перепрыгивают с крыши на крышу.
            Среди этих утренних ритуалов особенно выделялся книжный магазин на углу улицы. Он открывался ровно в девять, но за час до этого возле его двери уже собирались постоянные посетители. Владелец, седой мужчина по имени Павел, каждое утро выносил на крыльцо старую деревянную скамейку, ставил табличку «Скоро откроемся» и заваривал чай с бергамотом. Те, кто приходил раньше, не возмущались — они знали, что всё здесь живёт по своим правилам.
            Павел прожил в этом районе всю жизнь. Он видел, как менялись поколения, как зарастали травой дворы, как один за другим исчезали магазины, кафе и кинотеатры. Но его книжный выстоял. «Слово за словом» — так он назывался. Маленький, пыльный, с книгами, стоящими в два ряда, где под каждым изданием пряталась своя история. Не только та, что была напечатана на страницах, но и та, что оставили читатели: закладки из билетов, подчеркивания, случайные записки на полях.
            Однажды утром, когда Павел, как обычно, открывал магазин, он заметил у двери записку. Бумажка, приколотая кнопкой к деревянной раме, гласила: «Спасибо за книгу, которая спасла мне жизнь. Я вернусь». Подписи не было. Павел нахмурился, но ничего не сказал. Он положил записку в ящик стола и больше о ней не вспоминал.
            Прошло несколько недель. Книги продолжали менять руки, приходили и уходили покупатели, некоторые из них возвращались, становились завсегдатаями, обсуждали прочитанное, спорили, смеялись. Магазин жил своей неторопливой жизнью. А потом, в один дождливый день, дверь открылась, и на пороге появился молодой человек в тёмном пальто. Он снял капюшон, огляделся, как будто вспоминал что-то давно забытое, и только потом заговорил:
            — Здравствуйте. Я... был здесь раньше. Очень давно.
            Павел посмотрел на него, прищурился, будто пытаясь узнать в чертах что-то знакомое.
            — Возможно, — сказал он. — Лица у нас, как книги: можно забыть, но стоит открыть — и всё всплывает.
            Молодой человек кивнул. Он прошёл между стеллажей, задержался у полки с классикой, провёл пальцами по корешкам книг.
            — Вы оставили записку? — вдруг спросил Павел, не поворачивая головы.
            Тот не ответил сразу. Только через минуту, когда подошёл к стойке, он произнёс:
            — Да. Тогда я был на дне. И нашёл у вас «Старик и море». Казалось бы — простая история. Но именно она дала мне силы. Странно, да?
            — Не странно, — сказал Павел. — Книга — это не просто текст. Это отражение, компас. Иногда — единственный голос, который звучит в полной тишине.
            Молодой человек посмотрел на него с благодарностью. Потом достал из кармана другую книгу, аккуратно завёрнутую в бумагу.
            — Я хочу оставить это здесь. В знак благодарности.
            Павел взял свёрток, развернул его. Это был редкий экземпляр — первое издание «Преступления и наказания». На форзаце — подпись: «Для тех, кто ищет свет даже в самой глубокой тьме».
            Он ничего не сказал, только слегка кивнул.
            С того дня молодой человек стал приходить чаще. Он не всегда что-то покупал, но каждый раз приносил с собой истории. Иногда рассказывал о людях, которых встречал, иногда — о книгах, которые читал. А иногда просто сидел в углу и читал, не произнося ни слова.
            Магазин стал меняться. Появились новые книги, новые лица. Но дух старого «Слово за словом» остался. Потому что такие места — это не просто магазины. Это якоря. Места, где человек может вспомнить, кто он есть. Где слова становятся спасением. Где незнакомец с улицы может стать частью чего-то большего.
            """;

}
