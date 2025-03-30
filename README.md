# Klient-Server aplikace s pipeline a dockerizací
Cílem bylo nasadit samostatně kontejnerizovaný server a kontejnerizovaného klienta s pomocí Jenkins pipeline.
Původně jsem použila EC2 t2.micro instanci na AWS, na ní běžel Jenkins konteiner, který měl přístup k host Docker deamonovi. 
Jenkins tak může vytvářet další konteinery na stejné instanci. 

### Apliekace
O kód aplikace mi příliš nešlo, pouze jsem chtěla dva moduly, klienta a server s websockety.

## Typ instavne EC2
Narazila jsem na stejnou situaci, jako [je popsaná zde](https://stackoverflow.com/questions/57991172/aws-ec2-t2-micro-unlimited-jenkins-maven-very-slow-build-hangs). 
Při buildování jednoho z modulů tedy došlo k chybě a server přestal pracovat. Problém šlo snadno vyřešit jiným typem instance

## Porty a docker
Velkou význou projektu bylo nastavení portů kontejnorů a jejich parsování včetně socketů.
