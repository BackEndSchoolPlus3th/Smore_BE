# JSON 객체를 생성하고 문자열로 변환
$jsonParams = @{
    host = @("smore-rds-instance.c7cca4g8ujfq.ap-northeast-2.rds.amazonaws.com")
    portNumber = @("3306")
    localPortNumber = @("3306")
} | ConvertTo-Json -Compress

# 큰따옴표를 이스케이프하여 JSON을 AWS CLI에 올바르게 전달
$jsonParams = $jsonParams -replace '"', '\"'

# AWS SSM 명령어 실행
aws ssm start-session `
  --target i-0d302d710d3bb27b0 `
  --document-name AWS-StartPortForwardingSessionToRemoteHost `
  --parameters "$jsonParams"
