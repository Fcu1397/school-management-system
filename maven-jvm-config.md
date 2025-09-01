# Maven JVM 參數設置
# 用於消除 Java 24 與 Maven 3.9.11 的相容性警告

# 方法1: 在命令行中設置環境變數
export MAVEN_OPTS="--add-opens java.base/sun.misc=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED"

# 方法2: 在 .zshrc 或 .bashrc 中添加上面的環境變數設置

# 方法3: 使用 .mvn/jvm.config 文件（推薦）
