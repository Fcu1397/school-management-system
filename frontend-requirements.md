# 個人資料管理功能 - 前端開發需求

## 專案背景
學校管理系統的個人資料管理模組，需要實作使用者個人資料的查看和編輯功能。後端 API 已完成，需要前端介面來呈現和操作這些功能。

## 後端 API 規格

### 基礎 URL
```
http://localhost:8080/api
```

### API 端點

#### 1. 獲取個人資料
```http
GET /api/profile/{userId}
```

**回應格式：**
```json
{
  "success": true,
  "message": "獲取個人資料成功",
  "data": {
    "userId": 1,
    "email": "admin@school.edu",
    "fullName": "系統管理員",
    "phone": "02-12345678",
    "address": "台北市信義區",
    "birthDate": "1980-01-15",
    "gender": "其他",
    "profilePicture": "http://example.com/avatar.jpg",
    "roleName": "ADMIN",
    "createdAt": "2025-01-01T10:00:00",
    "updatedAt": "2025-01-01T10:00:00"
  }
}
```

#### 2. 更新個人資料
```http
PUT /api/profile/{userId}
Content-Type: application/json
```

**請求格式：**
```json
{
  "email": "newemail@school.edu",
  "fullName": "新姓名",
  "phone": "0912345678",
  "address": "新地址",
  "birthDate": "1990-01-01",
  "gender": "男",
  "profilePicture": "http://example.com/avatar.jpg"
}
```

**回應格式：**
```json
{
  "success": true,
  "message": "個人資料更新成功",
  "data": {
    // 更新後的完整個人資料物件
  }
}
```

#### 3. 錯誤回應格式
```json
{
  "success": false,
  "message": "錯誤訊息",
  "data": null
}
```

## 資料驗證規則

### 前端驗證要求
1. **email**: 必填，需符合 email 格式
2. **fullName**: 選填，文字欄位
3. **phone**: 選填，電話號碼格式（支援 0-9、-、+、()、空格）
4. **address**: 選填，文字區域
5. **birthDate**: 選填，日期格式 (YYYY-MM-DD)
6. **gender**: 選填，下拉選單選項：「男」、「女」、「其他」
7. **profilePicture**: 選填，URL 格式

### 錯誤處理
- API 回應 `success: false` 時顯示 `message` 欄位的錯誤訊息
- 網路錯誤時顯示友善的錯誤提示
- 表單驗證失敗時顯示相對應的欄位錯誤

## UI/UX 需求

### 頁面結構
1. **個人資料檢視區**
   - 顯示目前的個人資料
   - 包含頭像預覽（如果有 profilePicture）
   - 顯示使用者角色和帳戶資訊

2. **個人資料編輯表單**
   - 可編輯的表單欄位
   - 即時驗證提示
   - 儲存和取消按鈕

3. **操作按鈕**
   - 「編輯」按鈕：切換到編輯模式
   - 「儲存」按鈕：提交更新
   - 「取消」按鈕：放棄編輯，恢復原始資料
   - 「重新載入」按鈕：重新從 API 獲取資料

### 使用者體驗
- 載入狀態指示器
- 成功/錯誤訊息提示
- 表單驗證即時回饋
- 響應式設計（手機和桌面相容）

## 測試資料

### 可用的測試帳戶
1. **管理員** (userId: 1)
   - email: admin@school.edu
   - fullName: 系統管理員
   - role: ADMIN

2. **學生** (userId: 2)
   - email: student@school.edu
   - fullName: 張小明
   - role: STUDENT

3. **教師** (userId: 3)
   - email: teacher@school.edu
   - fullName: 李老師
   - role: TEACHER

## 技術要求

### 必要功能
1. 使用 fetch API 或 axios 進行 HTTP 請求
2. 實作載入狀態管理
3. 表單驗證和錯誤處理
4. 響應式設計
5. 無障礙設計考量

### 建議的技術棧
- React/Vue/Angular（任選一個）
- CSS Framework（Bootstrap/Tailwind/Material-UI）
- 表單處理庫（Formik/React Hook Form 等）
- 狀態管理（Redux/Vuex/NgRx，如果需要）

### 可選增強功能
1. 頭像上傳功能（檔案上傳到雲端儲存）
2. 資料變更歷史記錄
3. 頭像裁切功能
4. 深色/淺色主題切換
5. 多語言支援

## 開發提示

### API 呼叫範例（JavaScript）
```javascript
// 獲取個人資料
async function fetchProfile(userId) {
  try {
    const response = await fetch(`http://localhost:8080/api/profile/${userId}`);
    const result = await response.json();
    
    if (result.success) {
      return result.data;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('獲取個人資料失敗:', error);
    throw error;
  }
}

// 更新個人資料
async function updateProfile(userId, profileData) {
  try {
    const response = await fetch(`http://localhost:8080/api/profile/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(profileData)
    });
    
    const result = await response.json();
    
    if (result.success) {
      return result.data;
    } else {
      throw new Error(result.message);
    }
  } catch (error) {
    console.error('更新個人資料失敗:', error);
    throw error;
  }
}
```

## 安全性考量

1. **輸入驗證**: 所有使用者輸入都需要在前端驗證
2. **XSS 防護**: 適當的輸出編碼，特別是顯示使用者輸入的內容
3. **CSRF 防護**: 如果後端有實作 CSRF token，前端需要配合
4. **資料敏感性**: 不要在前端儲存敏感資料

## 部署注意事項

1. **環境變數**: API URL 應該可以根據環境（開發/測試/生產）配置
2. **CORS**: 確保後端已設定適當的 CORS 政策
3. **快取策略**: 考慮個人資料的快取機制

## 預期交付物

1. 完整的個人資料管理頁面
2. API 整合和錯誤處理
3. 響應式設計實作
4. 基本的單元測試
5. 使用說明文件

請基於以上規格實作個人資料管理的前端介面，確保與後端 API 完美整合，提供良好的使用者體驗。
