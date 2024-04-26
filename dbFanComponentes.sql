CREATE DATABASE fanComponentes;

use fanComponentes;

CREATE TABLE ROLES(
	IDROL VARCHAR(4) PRIMARY KEY,
    NOMBRE VARCHAR(50) NOT NULL,
    CONTRASEÑA VARCHAR(50) NOT NULL
    );
    
    
INSERT INTO ROLES VALUES("E001", "NIDAE", "MUBARAK135k=");
INSERT INTO ROLES VALUES("C001", "FERMIN", "VANDALxYsh!");
INSERT INTO ROLES VALUES("E002", "AARON", "PLANETSIDE446g!");

CREATE TABLE COMPONENTES(
	IDCOMPONENTE VARCHAR(7) PRIMARY KEY,
    NOMBRE VARCHAR(400) NOT NULL,
    STOCK INT NOT NULL,
    PRECIO DOUBLE,
    DESCRIPCION VARCHAR(1000)
    );




INSERT INTO COMPONENTES VALUES ("CPU0001", "i7-7700k", 0, 319.77,"Con la tecnología Intel Turbo Boost 2.0, su ordenador ofrecerá una potencia y una gran capacidad de respuesta que le ayudarán a aumentar su productividad.");
INSERT INTO COMPONENTES VALUES ("CPU0002", "AMD RYZEN 7 7800X3D", 0, 409,"El procesador para juegos que domina el mundo de la mano de la tecnología AMD 3D V-Cache™, para ganar aún más rendimiento de juego. Sin importar la configuración ni la resolución que uses, lleva a tu equipo a la victoria con este maravilloso procesador para juegos. Disfruta, además, las ventajas de AMD 3D V-Cache™, la tecnología de punta que es sinónimo de latencia baja y mucho rendimiento de juego.");
INSERT INTO COMPONENTES VALUES ("CPU0003", "AMD RYZEN 5 5600X", 0, 154.99,"Juega con lo mejor. Seis núcleos increíbles para quienes simplemente desean jugar.");
INSERT INTO COMPONENTES VALUES ("CPU0004", "Intel Core i5-11400F", 0, 109.99,"Procesador de escritorio Intel® Core ™ i5-11400F de 11.ª generación sin gráficos de procesador. Con soporte PCIe Gen 4.0. Optimizado para juegos, creación y productividad. Se requieren gráficos discretos. Solución térmica incluida en la caja. Compatible con placas base basadas en chipset de la serie 500 y selecta serie 400. Consulte al proveedor de la placa base para conocer los detalles de compatibilidad. 65W.");

INSERT INTO COMPONENTES VALUES ("RAM0001", "Corsair Vengeance RGB Pro DDR4 3200", 0, 87.99,"La memoria con overclocking DDR4 Corsair Vengeance RGB Pro ilumina el PC con un efecto hipnótico gracias a la iluminación RGB dinámica multizona, además de ofrecer las mejores características de las DDR4 en cuanto a rendimiento.");
INSERT INTO COMPONENTES VALUES ("RAM0002", "Kingston FURY Beast RGB DDR4 3200MHz 16GB 2x8GB",0,45.99,"El módulo de memoria RAM DDR4 RGB FURY Beast de Kingston ofrece un aumento del rendimiento con unas velocidades fulgurantes y un estilo agresivo único con iluminación RGB. Esta deslumbrante y rentable actualización está disponible en frecuencias de 2666MHz a 3733MHz, latencias de CL15 a CL19, capacidades de 8GB a 32GB como módulo individual, y de 16GB a 128GB como kit. Su overclocking automático Plug N Play a velocidades de 2666 MHz está disponible en Intel XMP y Ryzen. El módulo FURY Beast DDR4 RGB se mantiene fresco gracias a su elegante y delgado disipador. Probada al 100% y garantizada de por vida, esta actualización es muy fácil y sin complicaciones para tu sistema Intel o AMD.");
INSERT INTO COMPONENTES VALUES ("RAM0003", "Corsair Vengeance DDR5 5600MHz 64GB 2x32GB", 0, 199, "CORSAIR VENGEANCE DDR5, optimizado para placas base Intel®, ofrece las frecuencias más altas y las mayores capacidades de la tecnología DDR5 en un módulo compacto de alta calidad que se adapta a su sistema. Los chips de memoria de alta frecuencia cuidadosamente seleccionados alimentan su PC a través de un procesamiento, renderizado y almacenamiento en búfer más rápidos que nunca, con regulación de voltaje incorporada para un overclocking fácil y finamente controlado. El software CORSAIR iCUE permite el monitoreo de frecuencia en tiempo real, la regulación de voltaje integrada y la personalización del perfil Intel XMP 3.0.");
INSERT INTO COMPONENTES VALUES ("RAM0004", "Corsair Vengeance RGB DDR5 6000MHz PC5-48000 32GB 2x16GB", 0, 124,"La memoria CORSAIR VENGEANCE RGB DDR5 ofrece rendimiento DDR5, frecuencias más altas y mayores capacidades optimizadas para placas base Intel®, mientras ilumina su PC con iluminación RGB de diez zonas dinámica y direccionable individualmente.");


INSERT INTO COMPONENTES VALUES ("GPU0001", "NVIDIA GeForce RTX 4060 EAGLE OC", 0, 343.99,"Las GPU NVIDIA® GeForce RTX® serie 40 son más que rápidas para jugadores y creadores. Cuentan con la tecnología de la arquitectura ultra eficiente NVIDIA Ada Lovelace, que ofrece un salto espectacular tanto en rendimiento como en gráficos con tecnología de IA. Disfruta de mundos virtuales realistas con trazado de rayos y juegos con FPS ultra altos y la latencia más baja. Descubre nuevas y revolucionarias formas de crear contenido y una aceleración de flujo de trabajo sin precedentes.");
INSERT INTO COMPONENTES VALUES ("GPU0002", "MSI GeForce RTX 3060 VENTUS 2X OC", 0, 279.99,"VENTUS ofrece un diseño centrado en el rendimiento que mantiene los elementos esenciales para realizar cualquier tarea. Una disposición de doble ventilador en un diseño industrial rígido permite que esta tarjeta gráfica de aspecto elegante se adapte a cualquier construcción.
RTX.  IT’S ON. Disfruta de los mayores éxitos de ventas de hoy como nunca antes con la fidelidad visual del trazado de rayos en tiempo real y el rendimiento definitivo de DLSS con tecnología de IA.
LA VICTORIA SE MIDE EN MILISEGUNDOS: NVIDIA Reflex ofrece la máxima ventaja competitiva. La latencia más baja.");
INSERT INTO COMPONENTES VALUES ("GPU0003", "MSI GeForce RTX 4070 Ti SUPER", 0, 899,"La serie GAMING SLIM es una variante más delgada de la serie GAMING que mantiene capacidades de alto rendimiento y una apariencia agresiva.");
INSERT INTO COMPONENTES VALUES ("GPU0004", "Zotac Gaming GeForce GTX", 0, 139.99,"La nueva generación de tarjetas gráficas ZOTAC GAMING GeForce GTX está aquí. Basado en la nueva arquitectura NVIDIA Turing, está equipado con memoria ultrarrápida GDDR6. Prepárate para ser rápido y fuerte.");


INSERT INTO COMPONENTES VALUES ("JOY0001", "Thrustmaster T16000M FCS", 0, 64.99,"El T.16000M FCS Flight Pack se ha diseñado para los pilotos avanzados que buscan controles realistas y completos como alternativa a usar un ratón y un teclado.
Está compuesto por la palanca de vuelo T.16000M FCS (Flight Control System) y el mando de potencia TWCS (Thrustmaster Weapon Control System) y el TFRP (Thrustmaster Flight Rudder Pedals).");
INSERT INTO COMPONENTES VALUES ("JOY0002", "Thrustmaster TCA Yoke Pack Boeing", 0, 497.18,"TCA YokePack Boeing Edition es un sistema de aviación con licencia oficial de Boeing y Xbox, para Xbox Oney Xbox Series X|S. También es compatible con PC.");
INSERT INTO COMPONENTES VALUES ("JOY0003", "Thrustmaster SimTask FarmStick Joystick", 0, 89.98,"El SimTask FarmStick es el joystick diseñado para controlar maquinaria agrícola y pesada, desarrollado según los conocimientos de Thrustmaster en simulación de vuelo. Con sus 3 ejes direccionales, 2 con sensores magnéticos H.E.A.R.T (HallEffect AccuRate Technology), puedes controlar con precisión carretillas elevadoras, grúas y todos los demás tipos de maquinaria agrícola y de construcción.
Plug & Play en Farming Simulator 22, el joystick multifuncional y los 33 botones de acción se asignan automáticamente a los controles de cada vehículo, para disponer de un fácil acceso. El diseño del stick se ha inspirado en los interiores de los tractores y equipos agrícolas modernos de alta gama, para lograr una mayor inmersión y realismo. El SimTask FarmStick es compatible con PC (Windows 11/10) y se combina perfectamente con un SimTask Steering Kit acompañado por un volante de carreras T128 o T248 (vendidos por separado), para dar lugar a una cabina del conductor inmersiva y llena de funciones.");

INSERT INTO COMPONENTES VALUES ("MOT0001", "GIGABYTE B550M AORUS ELITE mATX", 0, 103.00,"La placa base Gigabyte B550M AORUS ELITE soporta los procesadores AMD Ryzen de tercera generación.");
INSERT INTO COMPONENTES VALUES ("MOT0002", "MSI B650 GAMING PLUS WIFI", 0, 160.90,"B650 GAMING PLUS WIFI está diseñado con toneladas de conectividad, herramientas flexibles y una conveniente solución Wi-Fi con versión de memoria DDR5 para jugadores que quieren todo");
INSERT INTO COMPONENTES VALUES ("MOT0003", "MSI MAG B650 TOMAHAWK WIFI", 0, 232.99,"La serie MAG lucha junto a los jugadores en busca del honor. Con elementos de inspiración militar agregados en estos productos de juego, renacieron como el símbolo de robustez y durabilidad.");
INSERT INTO COMPONENTES VALUES ("MOT0004", "MSI B450M-A PRO MAX", 0, 62.99, "Placa base AMD AM4 inspirada en el diseño arquitectónico, con Core Boost, DDR4 Boost, Turbo M.2, USB 3.2 Gen1");

INSERT INTO COMPONENTES VALUES ("ALI0001", "Nox Hummer GD750 750W 80 Plus Gold", 0, 91.98,"Hummer GD, son las fuentes con certificado 80 PLUS Gold de Nox. Se trata de fuentes que garantizan una eficiencia del 90%, diseñadas para aquellos que necesitan una potencia estable y una garantía de rendimiento en equipos de altas prestaciones.");
INSERT INTO COMPONENTES VALUES ("ALI0002", "Mars Gaming MPIII750 Fuente Alimentación PC 750W ATX", 0, 46.98, "FUENTE DE ALIMENTACIÓN MPIII750. 5 años de garantía gracias a la última tecnología de fabricación SMD de alta precisión, PCB de doble capa de fibra de vidrio y componentes grado-A de fiabilidad total. Silencio máximo con la nueva tecnología AI-RPM. 99% eficiencia DIGITAL APFC, 85% de eficiencia eléctrica, cables planos negros extra-largos y todos los sistemas de protección y seguridad incluidos.");
INSERT INTO COMPONENTES VALUES ("ALI0003", "Corsair CV Series CV650 650W 80 Plus Bronze V2", 0, 69.97, "Las fuentes de alimentación CORSAIR CV son perfectas para alimentar su nuevo PC en casa o en la oficina, con la eficacia 80 PLUS Bronze garantizada para proporcionar una potencia plena continuamente al sistema.");
INSERT INTO COMPONENTES VALUES ("ALI0004", "Tempest PSU 750W", 0, 50.99,"Tempest una vez más presenta una
 fuente para conquistar el mercado gaming. Por ello os presentamos la nueva fuente de 750W. Esta nueva versión mejorada
 hemos querido implementar todas aquellas mejoras a nivel interno que nos solicitaban nuestros usuarios.");

INSERT INTO COMPONENTES VALUES ("REF0001", "Tempest Liquid Cooler 360 ARGB Kit de Refrigeración Líquida", 0, 134.99, "Refrigeracion liquida de la mano de Tempest Liquid con un radiador incluido y dos tubos RGB");
INSERT INTO COMPONENTES VALUES ("REF0002", "Forgeon Azoth 360 ARGB", 0, 139.99, "Forgeon nos presenta su modelo mejorado de refrigeración líquida. El Kit Forgeon Azoth 360 ARGB de color Negro. Un modelo superior que incorpora 3 ventiladores, con iluminación RGB donde tienes la posibilidad de elegir entre sus múltiples efectos. También incorpora una bomba con efecto infinito ajustable en 12 posiciones donde contarás con un rendimiento térmico de hasta 360 W. Sin duda una auténtica bestia de la refrigeración.");
INSERT INTO COMPONENTES VALUES ("REF0003", "Tempest Liquid Cooler 120 Kit", 0, 50.99, "Tempest Liquid Cooler 120 Kit Liquid Cooler Negra es la solución perfecta para mantener tu sistema gaming fresco y potente sin la distracción de la iluminación RGB. Este refrigerador líquido de alto rendimiento ha sido diseñado para enfriar eficientemente tu procesador, garantizando temperaturas bajas y una vida útil prolongada para tu equipo");
INSERT INTO COMPONENTES VALUES ("REF0004", "MSI MAG CoreLiquid 360R V2 Kit ", 0, 109.99, "La nueva refrigeración líquida MAG CORELIQUID Series tiene todo lo que buscas, materiales de alta calidad que facilitan la disipación del calor de forma extremadamente efectiva. Cómo no, cuenta con iluminación ARGB y un bloque capaz de rotar hasta 270 grados. El diseño del bloque es único y asimétrico. Cada componente individual de MAG CORELIQUID Series ha sido diseñado con un único fin: Conseguir la mejor disipación.");








