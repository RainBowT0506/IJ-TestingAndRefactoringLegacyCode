實作影片 [Testing and Refactoring Legacy Code](https://www.youtube.com/watch?v=LSqbXorkyfQ)

Github [IJ-TestingAndRefactoringLegacyCode](https://github.com/RainBowT0506/IJ-TestingAndRefactoringLegacyCode)

# Introduction
## 介紹
本段介紹了一場有關測試和重構舊代碼的討論會，由 Mala Gupta 主持，並邀請到 Sandra Mancuso 作為專家來分享她在醫療保健領域中一個龐大軟體現代化項目的經驗。

* Mala Gupta 是主持人，而 Sandra Mancuso 則是受邀的專家。
* Sandra 是 Codudens 的聯合創始人，同時也是倫敦軟件工藝社區的創始人，並且是《The Software Craftsman》的作者。
* Sandra 目前正在領導一個醫療保健領域的大型軟件現代化項目，該項目涉及一個有 20 年歷史的 Java 應用程式，擁有超過 1600 萬行代碼，並有來自多個地點的 450 名開發人員參與。

##  主持人與嘉賓介紹
在介紹主持人和嘉賓後，提到了一些有趣的細節，例如：

* Sandra 不是足球迷，而是芝加哥公牛隊的狂熱支持者。
* 會議的一些細節，包括觀眾可以在 YouTube 聊天中提問問題，並在會議結束時由 Sandra 和 Anna Kozlova 回答。

## 補充說明
在會議開始之前，Sandra 強調了參與者需要對展示的小型程式碼保持開放的想像力，即使這只是一小部分，但其中包含許多大型程式碼中常見的問題。他呼籲參與者專注於展示的技術和技巧，並表示他會盡量展示多種技術，雖然這些技術並不都適用於示例程式碼。
# Overview & Ground Rules
## 概述

在這一部分，Sandra Mancuso 介紹了一個名為 "trip service" 的小型程式碼，並強調了在舊代碼上進行重構的目標和限制。

* 這個 "trip service" 是一個真實應用程式的一部分，用 Java 和 JSF 寫成，用於旅行者社交網絡。
* 這段程式碼的主要業務規則是需要登錄才能查看內容，並且只有被其他用戶設置為好友的情況下，才能查看其旅行記錄。
* 在重構舊代碼時，首先需要將其測試覆蓋率提升到 100%，然後才能進行重構和改進。
* 重構舊代碼的挑戰包括對現有程式碼進行單元測試、處理硬編碼的依賴關係以及保持公共接口的穩定性等。

## 重構的基本原則

* 在重構舊代碼時，需要先對其進行單元測試，並確保測試覆蓋率達到 100%。
* 使用自動化重構工具（如 IntelliJ）來修改生產代碼，以確保行為不會受到影響並減少引入錯誤的風險。
* 在進行測試時，需要保持公共接口的穩定性，避免對模塊或類的公共接口進行重大更改，以免破壞其他部分的功能。
* 需要注意引入狀態可能導致的多線程問題，並盡量保持類的無狀態性。
 
 
# Deep Shallow Branches
## 深淺分支概念
![image](https://github.com/RainBowT0506/IJ-TestingAndRefactoringLegacyCode/assets/109667537/0e175066-05be-4ac3-8bd7-db361c5ca669)
![image](https://github.com/RainBowT0506/IJ-TestingAndRefactoringLegacyCode/assets/109667537/aa9c1904-3cac-426d-be36-1a9ebcfc38a9)

- 深淺分支是指在程式碼中的不同路徑，淺分支是指從呼叫公開方法到退出該方法所需的最短路徑。
- 這種方法可以幫助開發者在理解程式碼時逐步建立知識，從最短的分支開始，一步步地擴展對程式碼的了解。
- 透過分析程式碼的淺分支，撰寫相對應的測試，並逐步建立測試覆蓋率，可以更有效地確保程式碼的品質。

## 應用與好處
- 透過深淺分支的方法，可以將大型程式碼拆解成小片段，有助於開發者更容易理解、測試和維護程式碼。
- 在測試時，從最淺的分支開始，逐步擴展測試覆蓋範圍，確保所有程式碼路徑都經過測試，提高程式碼的健壯性和可靠性。
- 在重構時，從最深的分支開始，可以確保重構的部分已經經過充分的測試，降低了重構可能引入的風險。

## 執行步驟
```
@Throws(UserNotLoggedInException::class)
fun getTripsByUser(user: User?): List<Trip> {
    var trips: List<Trip> = listOf()
    val loggedUser = UserSession.getInstance().getLogged()
    var isFriend = false

    user?.let {
        for (friend in user.getFriends()) {
            if (friend.equals(loggedUser)) {
                isFriend = true
                break
            }
        }
        if (isFriend) {
            trips = TripDAO.findTripsByUser(user);
        }

        return trips
    } ?: throw UserNotLoggedInException()
}
```
* 觀察程式碼的結構，從最淺的分支開始逐步理解程式碼的執行流程。
    針對 getTripsByUser 方法，只需知道如何執行完程式碼與拋出例外 `UserNotLoggedInException`。
* 撰寫測試案例，以描述每個淺分支的行為。
* 透過測試來驗證每個淺分支的功能，確保程式碼的準確性和穩定性。
* 進行重構時，從最深的分支開始，確保已經通過了足夠的測試覆蓋。

## 解決依賴問題
- 在測試時，可能會遇到程式碼的硬編碼依賴問題，例如靜態方法的呼叫或是外部依賴的存在。
- 解決這些問題的方法之一是創建一個“場景”（scene），即在程式碼中創建一個明確的接合點，使兩個類之間的依賴關係更清晰。
- 通過創建場景，可以使測試更容易進行，並且可以有效地測試程式碼的不同分支和情況。
 
# Running the Test

## 簡介
- 作者討論了在進行測試時的一些技巧和最佳實踐，著重於提高測試的可讀性和敘述性。

## 分離硬編碼的依賴性
- 作者提到使用測試來分離硬編碼的依賴性，以及如何通過實例化部分物件來達到這個目的。
- 強調了在修改測試目標時不必打破整個測試，而是逐步分離依賴性。

## 重構測試
- 引入概念，例如"logged-in user"，以使測試的敘述更豐富。
- 創建一個用於描述“logged-in user”狀態的字段，使測試的意圖更加清晰。
- 提到避免使用`null`，而是使用常數表示“guest”或“anonymous”狀態。

## 代表性的測試案例
- 創建一個表示“guest”狀態的常數，以更清晰地表達測試的目標。
- 討論測試的意圖是驗證“logged-in user”的情況，並著重於透過代表性的測試案例來增強測試的表達力。

## 使用程式碼覆蓋率工具
- 探討使用IntelliJ的程式碼覆蓋率工具，強調其在確保測試涵蓋範圍的重要性。
- 提及調整IntelliJ的設置以更清晰地顯示程式碼覆蓋率。

## 探索更深層次的分支
- 引入“淺分支到深層次分支”的概念，鼓勵逐步探索和測試程式碼中更深層次的邏輯分支。
- 通過解釋紅色部分表示尚未測試的程式碼，強調了在覆蓋程式碼中更深層次分支的必要性。

## 測試不同情境
- 提到下一個測試將涉及確保在“users are not friends”情境下，`trip service`應該返回空的`trips`。
- 使用`assertJ`來簡化測試案例的編寫。

## 進一步玩弄測試資料
- 強調可以透過玩弄測試資料，特別是輸入資料，來實現更高的程式碼覆蓋率。
- 示範了修改使用者對象以達到更多分支覆蓋的例子。
 
# The Stranger 
## 引入新角色
- 作者介紹了一個新的角色，即“stranger”，用於進一步擴展測試案例。
- "Stranger" 指的是一個未登錄用戶，用於測試當前程式邏輯下的行為。

## 設定 Stranger 物件
- 將 "Stranger" 物件配置為具有特定的屬性，包括朋友和旅行記錄。
- 這些屬性的設置用於模擬真實世界中的使用情境，以便更好地理解程式碼的行為。

## 測試案例意圖
- 強調了測試案例的意圖是確保當"stranger"嘗試訪問旅行記錄時，系統是否按照預期返回空值。
- 這澄清了測試的目標和預期結果，使測試案例更具可讀性和可理解性。

## 進一步的測試進展
- 通過引入 "Stranger" 物件和相關屬性，測試案例的內容和覆蓋範圍得到了進一步的擴展。
- 作者強調了測試案例的進化，並暗示了未來可能的更多測試方向和發展。 

 
# Test Cleanup 
## 測試清理的重要性
- 作者強調了測試清理的重要性，並指出過度複雜的測試可能會讓代碼更難以維護。
- 指出過度複雜的測試可能會導致測試失效，增加維護成本，並抑制人們對測試驅動開發和單元測試的積極性。

## 測試擴展與重構
- 討論了測試的不斷擴展和重構，並指出過多的測試設置和冗長的測試代碼可能會使測試變得笨重和難以管理。
- 強調了保持測試清潔並降低冗餘代碼量的重要性，以減少維護成本並提高測試的可讀性和可維護性。

## 重構測試設置
- 提出了將測試設置移至單獨的設置方法的建議，以使測試方法更加清潔和精簡。
- 強調了透過重構測試設置，可以使測試代碼更具可讀性和易於理解。

## 下一步清理
- 作者提到測試仍然有一些冗餘和冗長的部分，但在進一步重構之前，他決定先進行下一個測試案例的編寫。 
 
# Test Friends 
## 添加新的測試案例
- 作者提到缺少的最後一個測試案例，即當用戶是朋友時，`trip service`應該返回旅行記錄。

## 更新測試設置
- 將測試案例的設置部分移到新的測試中，以測試用戶之間的友好關係。
- 通過設置用戶之間的友好關係，模擬了真實世界中的使用情境，使測試更具代表性。

## 測試案例預期結果
- 期望當用戶是朋友時，`trip service`應返回相應的旅行記錄，包括倫敦和巴塞羅那。
- 測試案例的預期結果更加具體，可以檢驗程式碼在不同情境下的正確性。

## 例外處理
- 作者提到測試失敗的原因是在訪問數據庫時引發異常。
- 測試中使用了虛構的例外情況，但在實際程式碼中，可能會出現實際的數據庫異常。
- 強調了對於異常情況的處理和測試的重要性。

## 重構測試方法
- 作者提到將相關的代碼片段提取到獨立的方法中，以提高代碼的可讀性和可維護性。
- 提出了將方法設置為 package private 的做法，以限制方法的可見性，從而促進更清晰的代碼結構。 

# Test Trips 
## 重複使用相同的技巧
- 作者表示將重複使用先前介紹的技巧，但在這裡將其應用於不同的情境。
- 介紹了一種新的方法，用於模擬數據庫訪問而不是直接訪問數據庫。

## 重構方法
- 強調了重構測試方法的重要性，以提高代碼的可讀性和可維護性。
- 提到將數據庫訪問替換為直接使用用戶類中的旅行記錄集合的方法，以達到測試目的。

## 利用現有資源
- 指出了如果用戶類中沒有旅行記錄集合，則可以採用類似的方法，將旅行記錄添加到測試中並從那裡返回。
- 這種技術利用了已有的資源，無需添加額外的代碼或功能。

## 測試運行與程式碼覆蓋
- 作者提到運行測試時，應該注意觀察程式碼覆蓋率，以確保測試覆蓋了程式碼的不同分支和邏輯。
- 測試的目的是驗證程式碼的正確性，並確保它符合預期行為。 
 
# Cleaning Up Tests 
## 測試清理的目標
- 作者提到現在所有的測試都通過，並且程式碼的測試覆蓋率達到了 100%。
- 指出除了兩個覆蓋率不足的方法外，其餘的程式碼都被測試覆蓋到了。

## 測試清理與重構
- 提出了一些測試清理和重構的想法，以使測試更加清晰和可維護。
- 建議將某些測試設置的邏輯抽取出來，並集中在測試的開始處執行，從而減少重複代碼並提高測試的可讀性。

## Given-When-Then 模式
- 介紹了 Given-When-Then 模式，即測試的三個主要區塊，用於清晰地描述測試的背景、操作和預期結果。
- 作者將測試案例分解為三個部分，以更清晰地描述測試的場景和預期結果。

## 使用建造者模式
- 提到當測試依賴於特定的實體或數據結構時，如果實體的建構函數或結構變化，測試就會受到影響。
- 建議使用建造者模式來創建測試所需的實體，從而保護測試並使測試更易讀。 
- 
# Creating a Builder & Using varargs
## 建立建造者模式
- 作者提到建造者模式的概念，用於創建複雜的對象。
- 建造者模式可以幫助管理測試中對於特定實體或數據結構的建立過程，以保護測試並提高可讀性。

## 創建 User Builder 類
- 作者開始創建 User Builder 類，用於建立用戶對象。
- 該類包含了一系列方法，用於設置用戶對象的各種屬性。

## 方法鏈式調用
- 在 User Builder 類中的每個方法都返回相同的 User Builder 實例，從而實現方法鏈式調用。
- 這種設計模式可以使測試代碼更加流暢且易於閱讀，同時減少了代碼重複性。

## 使用 varargs
- 作者介紹了在方法參數中使用 varargs 的概念，用於接受不定長度的參數列表。
- 這使得方法可以更靈活地接受多個參數，而無需事先定義固定數量的參數。

## 設置建造者的方法
- 在 User Builder 類中，作者使用 varargs 來處理用戶的朋友和旅行記錄。
- 這使得建造者方法更具彈性，可以接受任意數量的朋友和旅行記錄，從而使測試代碼更具通用性。

## 創建用戶對象
- 最後，作者展示了如何使用建造者模式來創建用戶對象，並將用戶的朋友和旅行記錄添加到用戶對象中。
- 這種方法保證了測試代碼的清晰性和可讀性，同時為測試提供了彈性和可擴展性。 

```.kt
val friend = User()
friend.addFriend(ANOTHER_USER)
friend.addFriend(REGISTERED_USER)
friend.addTrip(LONDON)
friend.addTrip(BARCELONA)
```

重構為 UserBuilder

```.kt
val friend = TripServiceTest.UserBuilder.aUser()
    .friendWith(TripServiceTest.ANOTHER_USER, TripServiceTest.REGISTERED_USER)
    .withTripsTo(TripServiceTest.LONDON, TripServiceTest.BARCELONA)
    .build()
```
# Testing 
## 使用建造者模式進行測試
- 作者展示了如何使用建造者模式來改進測試的可讀性和可維護性。
- 創建了一個 User Builder 類，用於建立測試中所需的用戶對象，並設置了用戶的朋友和旅行記錄。

## 重複測試案例的重構
- 作者將之前的測試案例重構為使用建造者模式，以減少重複代碼並提高測試的可讀性。
- 測試案例中的 Given-When-Then 模式清晰明確，描述了測試的背景、操作和預期結果。


# Extract Methods 

## 注意提取方法的潛在風險
- 在重構遺留程式碼時，第一個想到的是要提取方法。
- 然而，在提取方法時需要小心，因為某些程式碼結構可能存在程式異味（code smell）。
- 在這個案例中，for 迴圈代表了一個程式異味，稱為「特性嫉妒」。
- 特性嫉妒的情況發生在兩個類之間，一個類想要另一個類的特性或行為。

## 程式異味的原因和影響
- 「特性嫉妒」意味著一個類想要另一個類的資料或行為，而不是該類自己處理相關邏輯。
- 在這個案例中，TripService 前往 User 類，要求取得使用者的朋友清單，以進行檢查。
- 這樣的做法導致程式碼間的耦合性增加，降低了程式碼的可讀性和維護性。
- 由於這是一個關於旅行者的社交網路，這段程式碼是用來測試友誼關係的。
 
 
# Homeless Behavior 
## 行為的無家可歸
- 在社交網路上，這樣的 for 迴圈可能散佈在各處，因為需要大量測試友誼關係。
- 在社交網路上，測試友誼關係的需求可能十分龐大，涉及數十億個測試。
- 每當需要執行某項操作時，都需要先確認兩個使用者是否為朋友，這導致了大量重複的程式碼。

## Tell Don't Ask 的原則
- 為了解決特性嫉妒，可以使用一種稱為「Tell Don't Ask」的方法。
- 「Tell Don't Ask」是一種設計原則，意味著告訴一個物件要做什麼，而不是問它要資料。
- 而不是問使用者「給我你的朋友清單」，我們可以直接告訴使用者「你和另一個使用者是朋友嗎」。
 

 
# User Class 
## 衍生新行為
- 在 User 類中引入新的行為，以解決特性嫉妒的問題，這樣可以讓行為有一個明確的歸屬地。
- 在提取方法之前，我們可以考慮在 User 類中衍生出新的行為，因為這是一個新的行為，不是現有問題的一部分。

## 使用測試驅動開發
- 通過測試驅動開發的方式，確保新的行為符合預期並且能正常運作。
- 使用 TDD 的方式，可以先確保新行為的實現，然後再進行相應的重構和提取方法。

## 使用 User Builder
- 使用 User Builder 來創建使用者物件，這使得程式碼更加清晰和易於維護。
- 通過使用 User Builder，我們可以輕鬆地創建具有特定屬性和關係的使用者物件，減少了程式碼的重複性。

## 實現新方法
- 在 User 類中實現新的方法，這個方法是為了檢查使用者是否與另一個使用者為朋友。
- 透過實現這個方法，我們確保了新的行為在程式碼中的正確性和完整性。 
# Writing Tests 
## 測試撰寫的重要性
- 撰寫遺留程式碼的測試需要花費相當的時間和精力。
- 一旦測試完成，我們必須小心謹慎地進行後續的重構和修改。

## 避免過度重構
- 在完成測試後，不要過度重構程式碼，以免破壞已有的測試覆蓋率和功能性。
- 過度重構可能導致測試失敗，而且很難找出問題所在。

## 方法論和步驟
- 在與遺留程式碼一起工作時，必須非常有系統地進行操作。
- 每次修改程式碼後，都要運行測試，並觀察測試結果。
- 如果測試失敗，則必須回到先前的程式碼版本，以確保程式碼的正確性和功能性。 
# For Loops & Delete Variables 
## 將變數定義與使用接近化
- 在重構程式碼時，常見的困難之一是變數定義在程式碼的頂部，但在整個程式碼基底中使用。
- 一個技巧是將變數盡可能地靠近它們被使用的地方，這有助於提高程式碼的可讀性和可維護性。
- 當程式碼很長時（可能有200、300、500行），這種方法尤其有用，可以讓程式碼更易於管理。

## 重構 For Loop
- 原始的 For Loop 的主要目的是設置變數。
- 通過將變數定義與使用接近化，可以使程式碼更清晰和易於理解。
- 考慮將 For Loop 降級，以減少程式碼的複雜性並提高可讀性。 
## 最小化變數使用
- 將變數盡可能地靠近其使用的地方，這樣可以使程式碼更加簡潔和清晰。
- 減少不必要的變數定義，可以降低程式碼的複雜度，使重構過程更加順利。
- 在重構過程中，考慮移除不再需要的變數，以簡化程式碼結構並提高可維護性。 
# Guard Clause 
## Guard Clause 的重要性
- 每個方法都應該有明確的單一目的。
- 方法中的 Guard Clause 用於檢查方法是否可以執行其目的。
- Guard Clause 的存在是為了保護方法的行為，確保方法只在正確的狀態下執行。

## Guard Clause 的由來
- Guard Clause 一詞由 Bertrand Meyer 於 70 年代提出，他創建了一種名為 Eiffel 的語言。
- 在設計預先條件（preconditions）、後置條件（postconditions）和不變量（invariants）時，Guard Clause 扮演著重要角色。
- 在程式設計中，Guard Clause 主要用於檢查方法執行前的前提條件。

## Guard Clause 的重構
- Guard Clause 不應該改變類別的行為，而應該只是檢查條件。
- 通過反轉 if 條件，可以將例外狀況提升，從而分離出方法的真正行為。
- Guard Clause 的存在使得方法的行為與驗證條件分離，使得程式碼更易於理解和維護。 
# Hack
## 移除循環依賴
### 引入 Guard Clause
- 作者提到了在方法中引入 guard clause 的概念，用於確保方法在執行時處於正確的狀態。
- Guard clause 用於確保方法執行前的先決條件得到滿足，以防止方法執行出錯或不符合預期行為。

### 解決循環依賴
- 作者提到在程式中存在循環依賴的問題，導致模組間相互引用，使得程式結構較為混亂。
- 通過引入新的參數，將原本在不同模組間的依賴關係解開，降低耦合度，提高程式的可讀性和可維護性。

### 移除不必要的依賴
- 作者在移除循環依賴的過程中，介紹了一種移除依賴的技巧，即將原本的硬編碼依賴替換為參數傳遞的方式。
- 通過將依賴關係從方法中移除，使得程式碼更加清晰，並且減少了對具體實現的依賴，提高了程式的彈性和可測試性。

## 逐步移除 Hack
### 移除 Hack
- 作者提到了之前的一個 hack，即在 `TripService` 中的一個靜態方法，作者想要將其移除並進行測試。
- 通過將靜態方法轉換為實例方法，作者可以更容易地進行依賴注入和模擬，提高程式碼的可測試性和可維護性。
### 解決循環依賴
- 作者提到了解決循環依賴的技巧，即將一個類的方法從靜態轉換為實例方法，並在需要時進行依賴注入和模擬。 

## 抽象層級的提升
### 提升抽象層級
- 作者提到程式碼中存在著不同層級的抽象，有時會混合英文和程式語言，降低了程式碼的可讀性和易理解性。
- 作者建議將程式碼的抽象層級提升，將語言風格統一為高層次的抽象，使程式碼更易於理解和閱讀。

### 重構程式碼
- 作者將原來的程式碼進行了重構，將部分邏輯提取為更高層次的方法，並使用更清晰的命名和語言風格，提高了程式碼的可讀性和可維護性。 

## 重塑程式碼與工作流程的見解與經驗分享
### 提升程式碼語言一致性和抽象層級
- 建議將程式碼語言統一，並提高抽象層級，使公共方法更易於理解。
- 通過重構程式碼，將細節提取為高層次的抽象方法，提升程式碼的可讀性和可維護性。

### 採用敏捷開發方法和版本控制
- 主張敏捷開發方法，將問題拆解為小步驟進行持續迭代和改進。
- 強調使用版本控制工具（如Git）有效地管理和合作開發項目。

### 提高生產力和工作流程
- 建議使用強大的開發工具（如IntelliJ）提高生產力，並利用自動化功能減少重複工作。
- 採用細粒度的提交策略，使得回滾錯誤的決策變得更加容易。

### 專業精神和持續改進
- 強調對專業精神的追求和持續學習的重要性，鼓勵保持高標準的工作品質。
- 提倡團隊合作和有效溝通，鼓勵建設性的討論和反饋，以實現更好的結果。

### 故事化程式碼和創業經歷
- 強調將程式碼撰寫成故事的形式，提高可讀性和理解性。
- 分享創業經歷，並強調即使失敗也能從中學到寶貴的經驗和教訓。

### 核心觀點補充
- 重塑程式碼和工作流程的核心在於提高效率、可讀性和團隊合作。
- 持續學習、追求卓越、不斷改進是持續進步的關鍵。
- 專業的工具和開發環境有助於提高工作效率和代碼品質。

### 總結
作者分享了他在程式碼設計和工作流程中的寶貴經驗，強調了專業精神、持續學習和團隊合作的重要性。他的見解對於提高軟體開發團隊的效率和品質都有著重要的啟發作用。

## 亮點分享與問答總結
### 亮點分享
- 觀眾對Sandro的表現給予了熱烈的回饋，特別是在程式碼重構和工作流程優化方面的見解受到了高度讚揚。
- 觀眾特別喜歡Sandro闡述的將程式碼故事化的方法，以及對敏捷開發、版本控制和持續改進的強調。

### 經驗問答
- 在重構程式碼時，有觀眾提到可能會暴露使用者會話資訊的問題。Sandro解釋了這種情況下的權衡和解決方案，並強調了設計上的取捨和可行性。
- 關於測試覆蓋率，Sandro強調測試覆蓋率應該是測試的副作用，而不是主要目標。他建議開發人員專注於提高代碼的品質和可靠性，而不是盲目地追求特定的覆蓋率目標。

### 小結與補充
- 在重構代碼時，開發人員應該平衡技術和商業目標，避免暴露過多的實現細節，同時確保代碼的可讀性和可維護性。
- 在管理層與開發團隊之間，應該建立雙向溝通和理解，以確保項目的進展和成果。

### 結語
Sandro的經驗分享和見解為開發人員提供了寶貴的指導，鼓勵他們尋求在技術和管理上的平衡，以實現團隊和項目的成功。透過持續學習和改進，我們可以不斷提高我們的軟體開發水平，並創建更優秀的產品和代碼。

# 關鍵字
* 測試：確保軟體的正確性和可靠性的過程。
* 重構：改善軟體的內部結構，使其更易於理解和修改，而不會改變其外部行為。
* 遺留代碼：指舊的、過時的或不推薦使用的程式碼，通常指那些沒有進行重構或維護的程式碼。
* 軟體工匠《The Software Craftsman》：一種軟體開發者，強調軟體開發的專業技能、品質和精神。
- 重構（Refactoring）：在不改變程式碼外部行為的前提下，對程式碼進行修改，以改善其結構、可讀性和可維護性的過程。
- 單元測試（Unit Testing）：針對程式碼中的個別模塊或單元進行測試的自動化測試方法。
- 測試覆蓋率（Test Coverage）：指測試用例對程式碼中的行數、分支或其他結構元素的覆蓋程度，通常用百分比表示。
- 自動化重構（Automated Refactoring）：使用工具或腳本自動修改程式碼的過程，以保持程式碼行為不變的前提下進行重構。
- 公共接口（Public Interface）：程式碼提供給外部使用的部分，包括公開的方法、類型和介面等。
- 硬編碼的依賴關係（Hardwired Dependencies）：指程式碼中直接嵌入的依賴關係，使得程式碼難以進行單元測試或重構。
- 無狀態（Stateless）：指在程式碼中不包含任何狀態信息，每次調用都是獨立且相互獨立的。
* 淺分支（Shallow Branch）：程式碼中從呼叫某方法到退出該方法所需的最短路徑。
* 深分支（Deep Branch）：程式碼中包含較多邏輯或分支條件的路徑。
* 測試覆蓋率（Test Coverage）：指用於測試程式碼的測試案例所覆蓋的程式碼範圍百分比。
* 重構（Refactoring）：指修改程式碼的結構或設計，以改善其可讀性、可維護性和效能，而不改變其外部行為。
* 硬編碼依賴（Hardcoded Dependency）：指程式碼中直接使用的、難以更改或模擬的依賴關係。
* 場景（Scene）：在程式碼中創建的一個明確的接合點，用於處理兩個類之間的依賴關係，使測試更容易進行。
* Seam
- 硬編碼的依賴性：指在程式碼中直接嵌入的依賴關係，作者建議透過測試來分離這些依賴性。
- 測試目標：指正在進行測試的具體程式碼區域或功能，作者強調在修改測試目標時不必打破整個測試。
- 代表性的測試案例：指選擇具有代表性且能全面檢視程式碼邏輯的測試案例，以提高測試的質量。
- 程式碼覆蓋率工具：指用於評估測試案例對程式碼覆蓋範圍的影響的工具，例如IntelliJ的覆蓋率工具。
- 淺分支到深層次分支：指從測試程式碼的表面層次逐步擴展到更深層次的邏輯分支，以確保全面的程式碼測試。
- AssertJ：一個用於編寫Java測試案例的流暢式斷言庫，可以提高測試代碼的可讀性和表達性。
- "Stranger" 物件：一個新的角色或物件，代表未登錄用戶，用於測試當前程式邏輯下的行為。
- 朋友 (Friends)：指在系統中與特定用戶相關聯的其他用戶。
- 旅行記錄 (Trips)：指用戶在系統中創建或擁有的旅行記錄，通常用於模擬用戶活動或行為。
- 測試案例意圖：指測試案例所要檢驗的功能或行為，以及測試的預期結果。
- 測試進展：指測試案例的發展和擴展，通常隨著測試對象和需求的變化而變化。
- 測試清理：指對測試代碼進行重構和簡化，以減少冗餘代碼並提高可讀性和可維護性。
- 過度複雜的測試：指測試過於複雜，包含過多的設置和冗長的測試代碼，可能導致測試失敗率上升和維護成本增加。
- 測試設置：指在執行測試之前所需的準備工作，包括初始化物件、設定環境等。
- 測試重構：指對測試代碼進行結構和邏輯上的優化和調整，以提高代碼質量和可讀性。
- 友好關係：指用戶之間的相互聯繫或連結，通常在社交網路或應用程式中出現。
- 測試設置：指在測試案例中為測試環境做準備的步驟，例如初始化物件或設定初始狀態。
- 異常處理：指程式碼在運行時遇到意外情況時的處理方式，通常通過引發異常來識別和處理問題。
- 測試方法重構：指對測試代碼進行結構和邏輯上的調整和優化，以提高代碼的可讀性和可維護性。
- Package private：Java 中的一種訪問修飾符，表示該方法或類僅對同一 package 內的其他類可見，對於外部 package 不可見。
- 重複使用技巧：指在不同情境下應用相同的程式設計或測試技巧，以提高效率和一致性。
- 重構方法：指對測試方法進行結構和邏輯上的優化和調整，以提高代碼的可讀性和可維護性。
- 數據庫訪問：指程式碼與數據庫之間的交互過程，用於檢索或修改數據。
- 現有資源利用：指利用已有的資源或功能，而不是添加新的代碼或功能，以實現目標或測試需求。
- 程式碼覆蓋率：指測試案例所覆蓋的程式碼範圍，通常以百分比表示，用於衡量測試的完整性和效能。
- 測試清理：指對測試案例進行重構和清理，以減少重複代碼並提高可讀性和可維護性。
- 測試覆蓋率：指測試案例覆蓋程式碼的比例，用於評估測試的完整性和效能。
- Given-When-Then 模式：一種測試設計模式，將測試案例分為三個部分，即背景（Given）、操作（When）和預期結果（Then）。
- 建造者模式：一種設計模式，用於創建複雜對象，通過逐步設置其屬性來構建對象。在測試中，建造者模式可用於創建測試所需的實體或數據結構，以減少測試代碼的重複性。
- 建造者模式：一種創建型設計模式，用於創建複雜對象，並通過提供方法鏈式調用的方式來設置對象的各種屬性。
- varargs：Java 中的一種特性，允許方法接受可變長度的參數列表，使得方法可以接受不同數量的參數。
- 方法鏈式調用：一種編程風格，即在同一行上連續調用多個方法，每個方法都返回相同的對象實例，從而實現方法的連續調用。
- 彈性和可擴展性：指程式設計的能力，即代碼能夠靈活地適應不同的需求和情境，並且易於擴展和修改。
- 建造者模式：一種創建型設計模式，用於創建複雜對象，並通過提供方法鏈式調用的方式來設置對象的各種屬性。
- varargs：Java 中的一種特性，允許方法接受可變長度的參數列表，使得方法可以接受不同數量的參數。
- 方法鏈式調用：一種編程風格，即在同一行上連續調用多個方法，每個方法都返回相同的對象實例，從而實現方法的連續調用。
- 彈性和可擴展性：指程式設計的能力，即代碼能夠靈活地適應不同的需求和情境，並且易於擴展和修改。
- 建造者模式：一種軟體設計模式，用於創建和構建複雜對象，並提供一種更清晰和易於使用的方法來設置對象的屬性。
- 重構：指對程式碼的結構進行修改，以改進其設計、可讀性和性能，而不改變其功能。在測試中，重構可以使測試更加容易理解和維護。
- 提取方法（Extract Methods）：將一段程式碼片段提取出來，形成一個獨立的方法或函式，以提高程式碼的可讀性和重用性。
- 程式異味（Code Smell）：指程式碼中可能存在問題或改進空間的跡象，是需要注意和重構的部分。
- 特性嫉妒（Feature Envy）：一個類別對另一個類別的欲求的表現。這種情況下，一個類別似乎希望自己是另一個類別，並使用該類別的屬性或方法。- 提取方法（Extract Methods）：將一段程式碼片段提取出來，形成一個獨立的方法或函式，以提高程式碼的可讀性和重用性。
- 程式異味（Code Smell）：指程式碼中可能存在問題或改進空間的跡象，是需要注意和重構的部分。
- 特性嫉妒（Feature Envy）：一個類別對另一個類別的欲求的表現。這種情況下，一個類別似乎希望自己是另一個類別，並使用該類別的屬性或方法。
- Tell Don't Ask：一種設計原則，指的是告訴一個物件要做什麼，而不是問它要資料。這有助於降低程式碼的耦合度和增加封裝性。
- 特性嫉妒（Feature Envy）：一個類別對另一個類別的欲求的表現。這種情況下，一個類別似乎希望自己是另一個類別，並使用該類別的屬性或方法。
- 無家可歸的行為（Homeless Behavior）：指程式碼中的某些行為或操作沒有明確的歸屬地，可能散佈在程式的各處，使得程式碼難以理解和維護。
- 衍生新行為：在現有的類別中引入新的方法或行為，以滿足新的需求或解決問題。
- 測試驅動開發（Test-Driven Development, TDD）：一種軟體開發方法論，先寫測試，然後寫程式碼以滿足測試要求，確保程式碼的功能性和可靠性。
- User Builder：一種建構者模式的實現，用於創建使用者物件，提供了一種清晰且可讀性高的方式來建構物件。
- 提取方法（Extract Methods）：一種重構技術，將一段程式碼片段提取成為一個獨立的方法，以提高程式碼的可讀性和重複使用性。
- 測試覆蓋率（Test Coverage）：指的是測試案例對於程式碼的覆蓋程度，即測試是否涵蓋了程式碼的各個部分。
- 重構（Refactoring）：指對現有程式碼進行修改，以改善其結構、可讀性和維護性，而不改變其功能。
- 綠燈（Green）：在測試驅動開發中，表示所有測試均通過，程式碼功能正常。
- 紅燈（Red）：在測試驅動開發中，表示某些或所有測試失敗，程式碼功能存在問題或錯誤。
- 重構（Refactoring）：指對現有程式碼進行修改，以改善其結構、可讀性和維護性，而不改變其功能。
- 變數（Variables）：程式中用於存儲和表示數據的符號或名稱。
- For Loop：一種迴圈結構，在指定條件為真時重複執行某段程式碼，用於遍歷集合或執行重複操作。
- Guard Clause：在程式碼中用於檢查方法執行前提條件的語句或片段，以確保方法在正確的條件下執行。
- 前提條件（preconditions）、後置條件（postconditions）和不變量（invariants）：這些是由 Bertrand Meyer 在 Eiffel 語言中引入的概念，用於設計和驗證程式碼的正確性和行為。
- Hack：指短期內快速解決問題的一種不嚴謹的方法，通常是一個臨時性的解決方案，並且可能存在一定的風險。
- 依賴注入（Dependency Injection）：一種軟體設計模式，用於降低模組之間的耦合度。通過將依賴關係從類的內部移到外部，在運行時將依賴對象注入到類中。
- 模擬（Mocking）：測試中的一種技術，用於模擬外部系統的行為。通常用於測試中的某些組件，例如測試某個類的方法時，可以模擬其他類的行為以達到測試的目的。
- Guard Clause：一種編程技巧，用於在方法執行前檢查先決條件是否滿足，以防止方法執行出錯。通常用於確保方法在執行時處於正確的狀態。
- 循環依賴：指程式模組之間相互引用，形成一個閉合的依賴關係，使得模組之間的耦合度增加，降低了程式碼的可讀性和可維護性。
- 硬編碼（Hardwired）：指在程式中直接寫入具體的數值、路徑或引用，而不是通過變量、參數或配置文件來表示。
- 抽象層級（Abstraction Level）：程式設計中的概念，指的是程式碼中的程度，程式碼可以是低層次的實現細節，也可以是高層次的概念和邏輯。
- 提升抽象層級：將程式碼從低層次的實現細節轉換為更高層次的概念和邏輯，以提高程式碼的可讀性和易理解性。
- 重構（Refactoring）：指對程式碼進行結構調整和優化，以改進其設計、可讀性、可維護性和效能，而不改變其外部行為。
