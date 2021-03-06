# Phone Caller
Образовательный проект курса ["Многопоточность и сетевое взаимодействие в Android"](https://www.coursera.org/learn/android-multithreading-and-network) 

# Технологии 
Layout:
- SwipeRefreshLayout
- RecyclerView

Multitask:
- Loader (androidx.loader.content.Loader)
- AsyncTaskLoader (androidx.loader.content.AsyncTaskLoader)
    
Data:
- Content provider (android.provider.ContactsContract)

## Требования к проектоному заданию
### Необходимо перенести запрос к ContentProvider из главного потока в фоновый. Для этого нужно проделать следующие шаги:

1. Создать класс - наследник AsyncTaskLoader<String>.
2. Создать в нем строковое поле для хранения id (который приходит на вход в метод onItemClick()).
3. Добавить логику добавления этого id (через конструктор или сеттер)
4. Реализовать метод String loadInBackground(), скопировав в него текущую логику onItemClick(). Соответственно, в этом методе проходит запрос к контент провайдеру и возвращается найденный номер или null, если номера нет.

### В MainActivity:
- Добавляем интерфейс LoaderManager.LoaderCallbacks<String>, добавляем и реализуем методы. 
- onCreateLoader() должен возвращать созданный нами лоадер.
- В onLoadFinished() добавляем метод для проверки полученного номера (String data) и запускаем звонок или показываем тост с ошибкой, если номера нет.
- В методе onItemClick() запускаем вызываем LoaderManager и инициализируем Loader.


### Добавьте в MainActivity пункт меню в тулбар. Меню должно быть “ifRoom”
- При нажатии на это меню, запрос номера должен останавливаться.
- Чтобы успеть нажать на пункт меню после того, как нажали на элемент списка, добавьте двухсекундную задержку в метод loadInBackground() до запроса к контент провайдеру через метод TimeUnit.SECONDS.sleep(2).
- После отмены запроса, должен появиться тост с текстом “Запрос отменен”
- Без самого запроса при нажатии на пункт меню ничего не должно происходить.

## Функция получения контактов
```
    public String loadInBackground() {
        String number = null;

        Cursor cursor = getContext()
                .getContentResolver()
                .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                new String[]{mId, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
        }
        return number;
    }
```