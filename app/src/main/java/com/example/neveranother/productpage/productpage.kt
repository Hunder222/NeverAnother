package com.example.neveranother.productpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neveranother.R

@Composable
fun Productpage() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Navigation()

            Product()

            BorderLine()

            Measurement()

            BorderLine()

            Readmore(
                "Mød din nye yndlings-bh - skabt ved hjælp af vores custom fit-algoritme og skræddersyet til at passe perfekt til dine mål. Denne bh er skabt til dig.\n" +
                        "\n" +
                        "Det brede bånd giver støtte og stabilitet, så du føler dig sikker hele dagen. Den bløde, strikkede bøjle løfter og former blidt din silhuet, hvilket fremhæver din silhuet uden ubehaget ved en traditionel metalbøjle.\n" +
                        "\n" +
                        "De dobbeltlagede skåle har et ydre lag, der giver struktur og holdbarhed, mens det indre lag er blødt og åndbart. Siderne og ryggen har en luftig struktur, der holder dig kølig og komfortabel.\n" +
                        "\n" +
                        "Har justerbare stropper og tre hægtelukninger bagpå."
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductInfo(
                R.drawable.check_mark,
                "Størrelsesgaranti",
                "Vi ønsker, at du skal elske din bh. Pasformen er ikke perfekt? Vi tilbyder en gratis størrelsesgaranti! Se venligst ofte stillede spørgsmål nedenfor for at få mere at vide."
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductInfo(
                R.drawable.heart,
                "Leveringstid",
                "Vi bestræber os på at levere din bh inden for 6 uger. NEVER ANOTHER er en startup, og da vi arbejder med produktionsvinduer og skræddersyede produkter, er leveringstiderne længere end hvad fast fashion-branchen normalt kan tilbyde. Vi gør vores bedste for at minimere ventetiden, og vi takker dig for din tålmodighed."
            )

            Spacer(modifier = Modifier.height(16.dp))

            BorderLine()

            Dropdown("Materiale komposition", {
                DropdownContent2(
                    "41% Filea cupro (regenerated cellulose fiber derrived from cotton linter)\n" +
                            "25% Elastane  \n" +
                            "\n" +
                            "White \n" +
                            "34% Sensil® EcoCare recycled Nylon  \n" +
                            "\n" +
                            "Black \n" +
                            "34% Otex Nylon "
                )
            })

            BorderLine()

            Dropdown("Produktion", { DropdownContent2("Bh'en er strikket i ét stykke og fremstilles af vores dygtige produktionspartner i Holland, som har været en del af vores rejse og udviklingsproces fra starten.\n" +
                    "\n" +
                    "Vores valg af produktionspartner afspejler vores engagement i produkter af høj kvalitet, ordentligt arbejde og ansvarlig produktion.") })

            BorderLine()

            Dropdown("Vedligeholdelse", { DropdownContent2("Brug din bh i maksimalt 3 dage i træk, før du vasker den. Regelmæssig vask forlænger bh'ens levetid.\n" +
                    "\n" +
                    "30 grader finvask\n" +
                    "Brug en vaskepose, hvis det er muligt\n" +
                    "Vask med lignende farvet tøj\n" +
                    "Må ikke tørretumbles\n" +
                    "Brug ikke skyllemiddel\n" +
                    "Må ikke bleges") })

            BorderLine()

            Spacer(modifier = Modifier.height(10.dp))

            FAQ()
        }
    }
}


