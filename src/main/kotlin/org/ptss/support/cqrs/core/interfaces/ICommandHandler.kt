package org.ptss.support.cqrs.core.interfaces

interface ICommandHandler<in TCommand, TResult> {
    suspend fun handleAsync(command: TCommand): TResult
}