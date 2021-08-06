    package ru.aoit.hmc.rfid.ruslandata;

    import java.io.Serializable;

    @SuppressWarnings("serial")
    public class RfidData implements Serializable {

        @Tag(tag =   2, len =   2, comment = "Тип RFID")
        public Integer RFIDTYPE;

        @Tag(tag =   3, len =   2, comment = "Версия данных")
        public Integer CANISTER_VERSION;

        @Tag(tag =   4, len =   2, comment = "Объём, мл")
        public Integer CANISTER_VOLUME_ML;

        @Tag(tag =   5, len =  14, comment = "Производитель")
        public String CANISTER_MANUFACTURER_NAME;

        @Tag(tag =   6, len =   6, comment = "Дата выпуска")
        public String CANISTER_ISSUE_DATE_YYMMDD;

        @Tag(tag =   7, len =   6, comment = "Дата годности")
        public String CANISTER_EXPIRATION_DATE_YYMMDD;

        @Tag(tag =   8, len =  14, comment = "Активное вещество")
        public String CANISTER_ACTIVE_INGRIDIENT_NAME;

        @Tag(tag =   9, len =   2, comment = "Остаток, мл")
        public Integer CANISTER_RESIDUAL_VOLUME_ML;

        @Tag(tag =  10, len =  14, comment = "Наименование")
        public String CANISTER_NAME;

        @Tag(tag =  11, len =  14, comment = "Аббревиатура")
        public String CANISTER_ABBR;

        @Tag(tag =  12, len =   2, comment = "Расход, мл/м3")
        public Integer CANISTER_CONSUMPTION_ML_M3;

        @Tag(tag =  13, len =   2, comment = "Расход для промывки, мл/м3")
        public Integer CANISTER_CONSUMPTION2_ML_M3;

        @Tag(tag =  14, len =   2, comment = "Аэрация, мин")
        public Integer CANISTER_AERATION_MIN;

        @Tag(tag =  15, len =   1, comment = "Контрольная область")
        public Integer AREA1_SECTOR;

        @Tag(tag =  16, len =   1, comment = "Область производителя")
        public Integer AREA2_SECTOR;

        @Tag(tag =  17, len =   1, comment = "Область устройства")
        public Integer AREA3_SECTOR;

        @Tag(tag =  18, len =   1, comment = "Область безопасности")
        public Integer AREA4_SECTOR;

        @Tag(tag =  19, len =   1, comment = "Последний сектор")
        public Integer LAST_SECTOR;

        @Tag(tag =  20, len =   1, comment = "Страница тэгов")
        public Integer SWITCH;

        @Tag(tag =  21, len =   2, comment = "Версия данных помещения")
        public Integer ROOM_CARD_VERSION;

        @Tag(tag =  22, len =   2, comment = "Объем помещения, м3")
        public Integer ROOM_VOLUME_M3;

        @Tag(tag =  23, len =  14, comment = "Наименование организации")
        public String ROOM_ORGANIZATION_NAME;

        @Tag(tag =  24, len =  14, comment = "Наименование комнаты")
        public String ROOM_NAME;

        @Tag(tag =  25, len =   4, comment = "ID помещения")
        public Integer ROOM_ID;

        @Tag(tag =  26, len =   2, comment = "Тип помещения")
        public String ROOM_TYPE;

        @Tag(tag =  27, len =   2, comment = "Версия данных персонала")
        public Integer PERSON_CARD_VERSION;

        @Tag(tag =  28, len =  14, comment = "Наименование организации")
        public String PERSON_ORGANIZATION_NAME;

        @Tag(tag =  29, len =   4, comment = "ID организации")
        public Integer PERSON_ORGANIZATION_ID;

        @Tag(tag =  30, len =  14, comment = "Имя сотрудника")
        public String PERSON_NAME;

        @Tag(tag =  31, len =  14, comment = "Фамилия сотрудника")
        public String PERSON_SURNAME;

        @Tag(tag =  32, len =   4, comment = "ID сотрудника")
        public Integer PERSON_ID;

        @Tag(tag =  33, len =  14, comment = "Должность сотрудника")
        public String PERSON_POSITION;

        @Tag(tag =  34, len =   2, comment = "Уровень доступа")
        public Integer PERSON_ACCESS;

        @Tag(tag =  35, len =   2, comment = "Пин код")
        public Integer PIN_NUM;

        @Tag(tag =  36, len =  14, comment = "Наименование производителя метки")
        public String RFID_MANUFACTURER_NAME;

        @Tag(tag =  37, len =   6, comment = "Дата производства метки")
        public String RFID_ISSUE_DATE_YYMMDD;

        @Tag(tag =  38, len =   2, comment = "Версия данных метки")
        public Integer RFID_DATA_TYPE;

        @Tag(tag =  39, len =   4, comment = "Уникальный номер метки")
        public Integer UNIQUE_ID;

        @Tag(tag =  40, len =  64, comment = "Цифровая подпись")
        public byte[] DIGIT_SIG;

        @Tag(tag =  41, len =  14, comment = "Имя прибора")
        public String DEVICE_NAME;

        @Tag(tag =  42, len =   8, comment = "Уникальный номер прибора")
        public Long DEVICE_UNIQUE_ID;

        @Tag(tag =  43, len =  16, comment = "Имя точки доступа")
        public String WIFI_HOSTNAME;

        @Tag(tag =  44, len =  10, comment = "Пароль точки доступа")
        public String WIFI_PASSWORD;

        @Tag(tag =  45, len =   4, comment = "Счетчик моточасов")
        public Integer WORK_MINUTES;

        @Tag(tag =  46, len =   2, comment = "Начальная страница отчета")
        public Integer REPORTS_START_PAGE;

        @Tag(tag =  47, len =   2, comment = "Конечная страница отчета")
        public Integer REPORTS_STOP_PAGE;

        @Tag(tag =  48, len =   2, comment = "Указатель на страницу записи")
        public Integer REPORT_USED_PAGE;

        @Tag(tag =  49, len =   2, comment = "Указатель свободную страницу")
        public Integer REPORT_FREE_PAGE;

        @Tag(tag =  50, len =   4, comment = "UID")
        public Integer UID;

        @Tag(tag =  51, len =   4, comment = "REQ")
        public Integer REQ;

        @Tag(tag =  52, len =   4, comment = "ACK")
        public Integer ACK;

        @Tag(tag =  53, len =   4, comment = "NAK")
        public Integer NAK;

        @Tag(tag =  54, len =   4, comment = "RSP")
        public Integer RSP;

        @Tag(tag =  55, len =   4, comment = "MSG")
        public Integer MSG;

        @Tag(tag =  56, len =   4, comment = "ID")
        public Integer ID;

        @Tag(tag =  57, len =  64, comment = "Текстовое сообщение")
        public String TEXT;

        @Tag(tag =  58, len =   8, comment = "unix time")
        public Long START_TREATMENT_TIME;

        @Tag(tag =  59, len =   2, comment = "Время обработки")
        public Integer COUNTER_SEC;

        @Tag(tag =  60, len =   2, comment = "Количество жижи")
        public Integer COUNTER_LIQ_ML;

        @Tag(tag =  61, len =   1, comment = "Статус обработки")
        public Integer TREATMENT_STATE;

    }
