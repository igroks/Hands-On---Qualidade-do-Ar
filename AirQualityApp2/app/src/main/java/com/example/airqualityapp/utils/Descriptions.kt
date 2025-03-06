package com.example.airqualityapp.utils

data class Slide (
    val title: String = "",
    val image: String = ""
)

val slidePages = listOf(
    Slide(
        "Bem-vindo ao AirQuality!\nO seu sistema de monitoramento de qualidade do ar."),


    Slide(
        "Veja informações detalhadas sobre temperatura, umidade e poluentes."
    ),
    Slide(
        "Use o mapa interativo para explorar diferentes locais!"
    ),
    Slide(
        "Receba alertas quando a qualidade do ar estiver ruim."
    ),
)

val IQA_Range = listOf(
    Triple("Boa", "De 0 à 40", "Qualidade do ar ideal para respiração! Nenhum risco significativo conhecido à saúde. Seguro para todas as pessoas."),
    Triple("Moderada", "De 41 à 80", "Pessoas de grupos sensíveis (crianças, idosos e pessoas com doenças respiratórias e cardíacas) podem apresentar sintomas como tosse seca, espirros e fadiga. A população, em geral, não é afetada."),
    Triple("Ruim","De 81 à 120", "Toda a população pode apresentar sintomas como tosse seca, espirro, cansaço, ardor nos olhos, nariz e garganta. Pessoas de grupos sensíveis (crianças, idosos e pessoas com doenças respiratórias e cardíacas) podem apresentar efeitos mais sérios na saúde como dificuldade para respirar e agravamento de doenças como bronquite e asma."),
    Triple("Péssima", "De 121 à 200", "Toda a população pode apresentar agravamento dos sintomas como tosse seca, cansaço, espirro, ardor nos olhos, nariz, garganta e ainda apresentar sensação de sufocamento, crises asmáticas graves e inflamação no trato respiratório. Exposição prolongada pode levar a doenças pulmonares. Efeitos ainda mais graves à saúde de grupos sensíveis (crianças, idosos e pessoas com doenças respiratórias e cardíacas)."),
    Triple("Crítica", "Acima de 200", "Toda a população pode apresentar sérios riscos de manifestações de doenças respiratórias e cardiovasculares. Exposição prolongada pode levar a doenças graves, como câncer de pulmão e doenças crônicas. Aumento de mortes prematuras em pessoas de grupos sensíveis."),
)

val heatIndexExtraInfo = listOf(
    Triple(5, "Confortável", "Sem ríscos significativos de saúde."),
    Triple(4, "Moderado", "Cansaço possível após exposição prolongada."),
    Triple(3, "Perigo moderado", "Exaustão pelo calor e cãibras são possíveis."),
    Triple(2, "Perigo Alto", "Golpe de calor provável com exposição prolongada."),
    Triple(1, "Perigo Extremo", "Risco iminente de golpe de calor.")
)

val windChillExtraInfo = listOf(
    Triple(5, "Confortável", "Sem riscos associados ao frio."),
    Triple(4, "Frio leve", "Desconforto possível. Proteja-se ao ar livre."),
    Triple(3, "Frio moderado", "Risco de hipotermia e frostbite com exposição prolongada."),
    Triple(2, "Perigo", "Congelamento em 30 minutos sem proteção."),
    Triple(1, "Perigo Extremo", "Congelamento em até 10 minutos.")
)

val dewPointExtraInfo = listOf(
    Triple(5, "Seco", "Ar confortável, mas pode ressecar pele."),
    Triple(4, "Confortável", "Condições ideais para a maioria das pessoas."),
    Triple(3, "Ligeiramente Úmido", "Começa a parecer abafado."),
    Triple(2, "Desconfortável", "Suor evapora com dificuldade."),
    Triple(1, "Muito Desconfortável", "Extremamente abafado. Risco de superaquecimento.")
)

val pm25ExtraInfo = listOf(
    Triple(5, "Bom", "Poeira fina, gotículas d'água, partículas naturais de solo e mar, emissões de vegetação."),
    Triple(4, "Moderado", "Fumaça leve de queima de madeira ou cigarro, emissões de carros e caminhões, poluição urbana."),
    Triple(3, "Ruim","Queima de combustível em indústrias e veículos, incêndios florestais, poluição intensa."),
    Triple(2, "Péssimo", "Neblina tóxica, poluição densa, áreas próximas a incêndios ou fábricas poluentes."),
    Triple(1, "Critico", "Cenários de alto risco, como grandes queimadas, tempestades de poeira e regiões industriais com poluição extrema.")

)

val pm10ExtraInfo = listOf(
    Triple(5, "Bom", "Pólen de plantas, poeira leve, partículas de solo."),
    Triple(4, "Moderado", "Poeira de estradas não pavimentadas, obras, pequenas tempestades de areia."),
    Triple(3, "Ruim", "Poeira intensa, cinzas vulcânicas leves, emissões de fábricas, fumaça de queimadas."),
    Triple(2, "Péssimo", "Poeira pesada de construção, tempestades de areia, grandes incêndios."),
    Triple(1, "Crítico", " Tempestade Fortes de detrítos como terra e areia, cidades extremamente poluídas, áreas próximas a queimadas e desastres ambientais.")
)

val coExtraInfo = listOf(
    Triple(5, "Aceitável", "Concentração segura de monóxido de carbono. Nenhum risco à saúde."),
    Triple(4, "Em Alerta", "Pequeno aumento na concentração de CO. Pessoas mais sensíveis podem sentir leves sintomas como fadiga."),
    Triple(3, "Em Rísco", "Níveis de CO começam a impactar a saúde. Pode causar dores de cabeça e tontura em exposições prolongadas."),
    Triple(2, "Perígo", "Exposição pode causar sintomas graves, como náuseas e confusão mental. É necessário ventilação e precaução."),
    Triple(1, "Périgo Extremo", "Concentração perigosa de CO. Pode levar a intoxicação severa e risco de morte em exposições prolongadas. Ventilar o ambiente imediatamente.")
)