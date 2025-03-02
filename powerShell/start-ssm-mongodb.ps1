# JSON 객체를 생성하고 문자열로 변환
$jsonParams = @{
    host = @("smore-docdbelastic-cluster-890742594012.ap-northeast-2.docdb-elastic.amazonaws.com")
    portNumber = @("27017")
    localPortNumber = @("27017")
} | ConvertTo-Json -Compress

# 큰따옴표를 이스케이프하여 JSON을 AWS CLI에 올바르게 전달
$jsonParams = $jsonParams -replace '"', '\"'

# AWS SSM 명령어 실행
aws ssm start-session `
  --target i-039e751d7fc2e2be6 `
  --document-name AWS-StartPortForwardingSessionToRemoteHost `
  --parameters "$jsonParams"
