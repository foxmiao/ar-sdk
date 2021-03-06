//
//  BARMainController+Public.h
//  AR-Base
//
//  Created by Asa on 2018/3/28.
//  Copyright © 2018年 Baidu. All rights reserved.
//

#import "BARMainController.h"
#import "BARImageMovieWriter.h"

//ARType：
/*
  kBARTypeLocalSameSearch || kBARTypeCloudSameSearch - - - Do same searching by yourself
  kBARTypeARKit == arType - - - Currently not supported
  Other - - -Call startAR
 */
typedef void(^BARLoadSuccessBlock)(NSString *arKey, kBARType arType);
typedef void(^BARLoadFailedBlock)(void);

@interface BARMainController (Public)
/**
 Loading process：pass ARKey-->Query downloadAR-->Load AR-->Callbacks to callers
 @param arKey Generated ARkey
 */
- (void)loadAR:(NSString *)arKey success:(BARLoadSuccessBlock)successBlock
 failure:(BARLoadFailedBlock)failureBlock;

- (void)stopARState;
- (void)startARState;

- (void)setRenderMovieWriter:(BARImageMovieWriter *)movieWriter;
@end
