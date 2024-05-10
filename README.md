# <a name="_ehqt08w09yqu"></a>	**Jewelry Sales System At The Store**
<a name="_5eoyzn572g8y"></a>Software Requirement Specification

# <a name="_fkqr85w4d2ra"></a>**1.Team Member:**
- ## <a name="_w68qof78tzz7"></a>Nguyễn Ngọc Quy SE171134
- ## <a name="_bg37llco4uwn"></a>Phan Phạm Hòa SE171730
- ## <a name="_fj393ygqd98m"></a>Huỳnh Thái Tài SE171320
- ## <a name="_x6nr5sldp5qf"></a>Trần Trọng Nhân SE171973
# <a name="_kex60s1c7drv"></a>**2.Requirement:**
## <a name="_srcmrxiyu3np"></a>**2.1 Name of The System:**
### <a name="_w5fmk68csmvr"></a>**Jewelry Sales System At The Store**
### <a name="_s9krq3x2ti5"></a>(Phần mềm bán hàng trang sức tại cửa hàng)

## <a name="_1edxs1g18lnh"></a>**2.2 Purpose of The System:**
- ### <a name="_czpvqcxywgaj"></a>Creating a platform to support the staff to interact with customers easier.
## <a name="_zbonwbblfjcc"></a>**2.3 Actor**

|**No.**|**Actor**|**Description**|
| :- | :- | :- |
|1|Admin|User who take responsible for managing managers|
|2|Manager|User who take responsible for managing staffs|
|3|Staff|User who interacting with customers|
## <a name="_u6aap33vz8g8"></a>**2.4 List of Feature:**
### <a name="_bukxuss52tqa"></a>**Admin**

|**No.**|**Use case**|**Description**|
| :- | :-: | :- |
|1\.|Add Manager|Create a new account for manager role|
|2\.|Update Manager Info|Edit manager information, enable/disable manager account|
|3\.|Login|Log in into the system as an administrative role|
|4\.|View Dashboard|View dashboard statistics in an interval of time|

### <a name="_q1ykqcnxjsj7"></a>**Manager**

|**No.**|**Use case**|**Description**|
| :- | :-: | :- |
|1\.|Add Staff|Create a new account for manager role|
|2\.|Update Staff Info|Edit staff information, enable/disable staff account|
|3\.|Login|Log in into the system as a manager role|
|4\.|Add Accessory|Add new accessory into product list|
|5\.|Delete Accessory |Delete accessory in product list|
|6\.|Update Accessory|Edit accessory info in product list|
|7\.|View Product List|View list of product (name, price, stock) available in store|
|8\.|View Product Info|View product’s specified info such as name, price, stock, weight,...|
|9\.|View Counter Info|View the revenue and staff info of the counter under the management of the manager|
|10\.|Display Accessory|Display accessory info on external screen|
### <a name="_ri3rlguyx5e8"></a>**Staff**

|**No.**|**Use case**|**Description**|
| :- | :-: | :- |
|1\.|Add Item|Add a new item in the cart by scanning barcode or by inputting product code directly|
|2\.|Delete Item|Remove item from the card|
|3\.|Update Cart|Update item’s quantity in the card|
|4\.|Login|Log in into the system as a staff role|
|5\.|View Cart|<p>View the product in the cart and total price. </p><p>- ***Price of product*** = historical cost \*  gold price ratio.</p><p>- ***Historical cost*** = (Gold price at current time \* product weight) + manufacture price + stone price.</p>|
|6\.|Print Receipt|Print receipt for customer after payment|
|7\.|Print Warranty|Print warranty for each accessory that customers has purchased|
|8\.|Check out|Confirm the payment and change the stock|
|9\.|Refund|Reclaim the product that customer had bought from the store before. |
|10\.|View Product Info|View product information such as name, price, stock, weight,...|
## <a name="_iybf9vkh6owx"></a>**2.5 Statistics**
- ### <a name="_5ug0a6x246xj"></a>**Dashboard Statistics**
- #### <a name="_btr78ysm3c8j"></a>Total Revenue.
- ### <a name="_n5jjyx9dwkvz"></a>**Accessory Statistic**
  - #### <a name="_iktey6otpcci"></a>Product Name.
  - Price.
  - Stock.
  - Weight.
  - Main Stone.
  - Sub Stone.
  - Number of Main Stone.
  - Number of Sub Stone.
  - Brand.
  - Cut.
  - Gender.
- ### <a name="_e4q3kbu77li"></a>**Staff/Manager Info**
  - Full Name.
  - Address.
  - Role.
  - Phone Number.
  - Email.
  - Date of Birth.
## <a name="_q7fi7kbl5rz"></a>**2.6 Others**
- 1 manager manages 3 counters.

# <a name="_peiwz1qxl442"></a>**3.Policy:**
## <a name="_q6hg64meqm2a"></a>**3.1 Points Policy**
- The payment will be turned into points, with every 10 million vnd = 1 point.
- Points can be used in various activities such as discounts.
- The point will be printed within the receipt in a form of code. (User will input the code into their account gift code feature or in web gift code page)
## <a name="_c225aics878e"></a>**3.2 Discount Policy**
- 1 point = 100.000 vnd discount
- ### <a name="_uocvu7v7jcv2"></a>**Discount = Discount during promotion (optional) + Discount policy (optional)**
**Note:** For customers who can be affected by discount policy need approval of the manager before applying the discount into invoice.  
## <a name="_teq3mxl6k6qv"></a>**3.2 Purchasing Policy**
- For women’s accessory + normal stone, Shop will only buy the real gold part. 
- Reclaim the jewels which customers had bought with 70% sold price.
- The gold price will be based on a real-time price chart. 

## <a name="_uda3l5cdstlb"></a>**3.3 Warranty Policy**
- Every accessory can be returned to the shop within 7 days under these condition:
+ The accessory must remain intact and not have any sign of being used.
- Return procedure:
+ Step 1: Contact the staff to request returning product. (To facilitate the process, customer should provide warranty of the returning product)
+ Step 2: We will check the product’s warranty and product’s integrity then respond to the customer.
+ Step 3: We will perform a refund/replace procedure. (100% sold price)